package com.zhiyicx.baseproject.base;


import android.graphics.Canvas;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/2/7
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends ITSListPresenter<T>, T extends BaseListBean> extends TSFragment<P> implements ITSListView<T, P>, com
        .scwang.smartrefresh.layout.listener.OnRefreshListener, OnLoadmoreListener {
    /**
     * 默认每页的数量
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 有的地方是 10 条哦
     */
    public static final int DEFAULT_PAGE_SIZE_X = 10;

    /**
     * 一个页面显示的最大条数，用来判断是否显示加载更多
     */
    public static final int DEFAULT_ONE_PAGE_SIZE = 15;

    /**
     * 默认初始化列表 id
     */
    public static final Long DEFAULT_PAGE_MAX_ID = 0L;

    /**
     * 默认初始化列表分页，只对当 max_id 无法使用时有效，如热门动态
     */
    public static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_TIP_STICKY_TIME = 3000;
    public static final float DEFAULT_LIST_ITEM_SPACING = 0.5f;

    private static final boolean DEFAULT_NEED_REFRESH = false;

    protected List<T> mListDatas = new ArrayList<>();

    protected RecyclerView.Adapter mAdapter;

    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View mFooterView;

    protected SmartRefreshLayout mRefreshlayout;

    protected RecyclerView mRvList;

    protected View mFlTopTipContainer;
    protected TextView mTvTopTip;
    protected RecyclerView.LayoutManager layoutManager;
//    protected OverScrollLayout overscroll;

    /**
     * 因为添加了 header 和 footer 故取消了 adater 的 emptyview，改为手动判断
     */
    protected EmptyView mEmptyView;

    /**
     * 纪录当前列表 item id 最大值，用于分页
     */
    protected Long mMaxId = DEFAULT_PAGE_MAX_ID;

    /**
     * 只对当 max_id 无法使用时有效，如热门动态
     */
    protected int mPage = DEFAULT_PAGE;

    /**
     * 提示信息是否需要常驻
     */
    private boolean mIsTipMessageSticky;
    private View mTvNoMoredataText;

    /**
     * 最后一个 item 是否显示完了
     */
    private boolean mIsLastVisiable;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.finishLoadmore();
        mRefreshlayout.finishRefresh();
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
        hideLoading();
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mRefreshlayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshlayout);
        mRvList = (RecyclerView) rootView.findViewById(R.id.swipe_target);
        mFlTopTipContainer = rootView.findViewById(R.id.fl_top_tip_container);
        RxView.clicks(mFlTopTipContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onTopTipClick();
                    }
                });
        mTvTopTip = (TextView) rootView.findViewById(R.id.tv_top_tip_text);
        mEmptyView = (EmptyView) rootView.findViewById(R.id.empty_view);
        mEmptyView.setErrorImag(setEmptView());
        mEmptyView.setNeedTextTip(false);
        mEmptyView.setNeedClickLoadState(false);
        RxView.clicks(mEmptyView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onEmptyViewClick();
                    }
                });
        mRefreshlayout.setOnRefreshListener(this);
        mRefreshlayout.setOnLoadmoreListener(this);
        if (setListBackColor() != -1) {
            mRvList.setBackgroundColor(ContextCompat.getColor(getContext(), setListBackColor()));
        }
        layoutManager = getLayoutManager();
        mRvList.setLayoutManager(layoutManager);
        //设置Item的间隔
        mRvList.addItemDecoration(getItemDecoration());
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList.setHasFixedSize(sethasFixedSize());
        //设置动画
        mRvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = getAdapter();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooterWrapper.addFootView(getFooterView());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mRefreshlayout.setEnableAutoLoadmore(false);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // SCROLL_STATE_FLING; //屏幕处于甩动状态
                // SCROLL_STATE_IDLE; //停止滑动状态
                // SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
                if (AndroidLifecycleUtils.canLoadImage(getContext())) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(getContext()).resumeRequests();
                    } else {
                        Glide.with(getContext()).pauseRequests();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                getLastItemVisibility(mRvList);
            }

        });
//
//        overscroll = (OverScrollLayout) rootView.findViewById(R.id.overscroll);
//        overscroll.setOverScrollCheckListener(new OverScrollCheckListener() {
//            @Override
//            public int getContentViewScrollDirection() {
//                return OverScrollLayout.SCROLL_VERTICAL;
//            }
//
//            @Override
//            public boolean canScrollUp() {
//
//                if (mRefreshlayout.isRefreshEnabled()) {
//                    return true;
//                } else {
//                    // 如果不能够下拉刷新，并且到了顶部 就可以scrollUp
//                    if (!mRvList.canScrollVertically(-1)) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//
//            @Override
//            public boolean canScrollDown() {
//                // 如果能够上拉加载，就不能够overScroll Down
//                if (mRefreshlayout.isLoadMoreEnabled()) {
//                    return true;
//                } else {
//                    getLastItemVisibility(mRvList);
//                    // 如果不能够上拉加载，并且到了底部 就可以scrollUp
//                    if (mIsLastVisiable && !mRvList.canScrollVertically(1)) {
//                        onOverScrolled();
//                        return false;
//                    }
//                }
//                return true;
//            }
//
//            @Override
//            public boolean canScrollLeft() {
//                return false;
//            }
//
//            @Override
//            public boolean canScrollRight() {
//                return false;
//            }
//        });
    }

//    private void getLastItemVisibility(RecyclerView recyclerView) {
//        //得到当前显示的最后一个item的view
//        View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
//        if (lastChildView == null) {
//            mIsLastVisiable = false;
//            return;
//        }
//        //得到lastChildView的bottom坐标值
//        int lastChildBottom = lastChildView.getBottom();
//        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//        int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
//        //通过这个lastChildView得到这个view当前的position值
//        int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
//        //判断lastChildView的bottom值跟recyclerBottom
//        //判断lastPosition是不是最后一个position
//        //如果两个条件都满足则说明是真正的滑动到了底部
//        if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
//            mIsLastVisiable = true;
//        } else {
//            mIsLastVisiable = false;
//        }
//    }

//    protected void setOverScroll(Boolean topOverScroll, Boolean bottomOverScroll) {
//        if (overscroll != null) {
//            if (topOverScroll != null) {
//                overscroll.setTopOverScrollEnable(topOverScroll);
//            }
//            if (bottomOverScroll != null) {
//                overscroll.setBottomOverScrollEnable(bottomOverScroll);
//            }
//        }
//    }

    /**
     * 刷新数据的方式：方式1：启用下拉列表动画，调用onRefresh接口刷新数据 方式2：不启用下拉列表动画，仅仅调用刷新数据的方法
     *
     * @return needRefreshAnimation 是否需要启用下拉动画进行刷新
     */
    protected boolean isNeedRefreshAnimation() {
        return true;
    }

    /**
     * 通过getDataRefreshType的返回值，判断进行刷新的方式
     *
     * @return
     */
    private void getNewDataFromNet() {
        if (isNeedRefreshAnimation() && getUserVisibleHint()) {
            mRefreshlayout.autoRefresh();
        } else {
            mMaxId = DEFAULT_PAGE_MAX_ID;
            mPage = DEFAULT_PAGE;
            requestNetData(mMaxId, false);
        }
    }

    /**
     * 缺省图被点击
     */
    protected void onEmptyViewClick() {
        getNewDataFromNet();
    }

    /**
     * 获取没有更多的脚信息
     *
     * @return
     */
    protected View getFooterView() {
        // 添加加载更多没有了的提示
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_footer, null);
        mTvNoMoredataText = mFooterView.findViewById(R.id.tv_no_moredata_text);
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mFooterView;
    }

    /**
     * 如果确定每个item的内容不会改变RecyclerView的大小，设置这个选项可以提高性能
     *
     * @return
     */
    protected boolean sethasFixedSize() {
        return false;
    }

    /**
     * 当列表数据少于一页时，是否显示无更多数据
     *
     * @return
     */
    protected boolean showNoMoreData() {
        return false;
    }

    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }


    @Override
    protected void initData() {
        mRefreshlayout.setEnableRefresh(isRefreshEnable());
        mRefreshlayout.setEnableLoadmore(isLoadingMoreEnable());
        if (!isLayzLoad()) {
            // 获取缓存数据
            requestCacheData(mMaxId, false);
        }
    }

    protected void requestCacheData(Long maxId, boolean isLoadMore) {
        mPresenter.requestCacheData(mMaxId, isLoadMore);
    }

    @Override
    public void onResume() {
        super.onResume();
        layzLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        layzLoad();
    }

    private void layzLoad() {
        if (mPresenter != null && getUserVisibleHint() && isLayzLoad() && mListDatas.isEmpty()) {
            getNewDataFromNet();
        }
    }

    /**
     * 是否进入页面进行懒加载
     *
     * @return
     */
    protected boolean isLayzLoad() {
        return false;
    }

    /**
     * 进入页面是否自动调用下拉刷新请求新数据
     */
    protected boolean isNeedRefreshDataWhenComeIn() {
        return DEFAULT_NEED_REFRESH;
    }

    /**
     * 设置 LayoutManager 区分列表样式
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * 设置 item 间距数值
     *
     * @return 上下间距
     */
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
    }

    /**
     * An ItemDecoration allows the application to add a special drawing and layout offset
     * to specific item views from the adapter's data set. This can be useful for drawing dividers
     * between items, highlights, visual grouping boundaries and more.
     * <p>
     * <p>All ItemDecorations are drawn in the order they were added, before the item
     * views (in {@link RecyclerView.ItemDecoration#onDraw(Canvas, RecyclerView, RecyclerView.State) onDraw()}
     * and after the items (in {@link RecyclerView.ItemDecoration#onDrawOver(Canvas, RecyclerView,
     * RecyclerView.State)}.</p>
     */
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new LinearDecoration(0, ConvertUtils.dp2px(getContext(), getItemDecorationSpacing()), 0, 0);
    }

    /**
     * 是否需要上拉加载
     *
     * @return true 需要
     */
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    /**
     * 是否需要下拉刷新
     *
     * @return
     */
    protected boolean isRefreshEnable() {
        return true;
    }

    /**
     * 设置 list 背景色
     *
     * @return
     */
    protected int setListBackColor() {
        return -1;
    }

    /**
     * 适配器
     *
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 提示信息被点击了
     */
    protected void onTopTipClick() {
    }

    /**
     * 设置提示文本信息
     *
     * @param text 文本内容
     */

    protected void setTopTipText(@NotNull String text) {
        mTvTopTip.setText(text);
    }

    protected void setTopTipHtmlText(@NotNull String text) {
        Spanned html = Html.fromHtml(text);
        mTvTopTip.setText(html);
    }

    /**
     * Set the visibility state of this view.
     *
     * @param visibility
     * @attr ref android.R.styleable#View_visibility
     */
    protected void setTopTipVisible(int visibility) {
        mFlTopTipContainer.setVisibility(visibility);
    }

    /**
     * 显示提示信息，并消息
     *
     * @param text
     */
    @Override
    public void showStickyMessage(@NotNull String text) {
        mIsTipMessageSticky = true;
        setTopTipVisible(View.VISIBLE);
        setTopTipText(text);
    }

    /**
     * 显示提示信息，并消息
     *
     * @param html 网页数据
     */
    @Override
    public void showStickyHtmlMessage(@NotNull String html) {
        mIsTipMessageSticky = true;
        setTopTipVisible(View.VISIBLE);
        setTopTipHtmlText(html);
    }

    /**
     * 隐藏常驻提示信息
     */
    @Override
    public void hideStickyMessage() {
        mIsTipMessageSticky = false;
        setTopTipVisible(View.GONE);
    }

    /**
     * 显示提示信息，并消息
     *
     * @param text
     */
    protected void showMessageNotSticky(@NotNull String text) {
        // 如果有常驻信息在，忽略此条提示
        if (mIsTipMessageSticky) {
            return;
        }
        setTopTipVisible(View.VISIBLE);
        setTopTipText(text);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTopTipVisible(View.GONE);
            }
        }, DEFAULT_TIP_STICKY_TIME);
    }

    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore);
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData() {
        setEmptyView();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void setEmptyView() {
        if (mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData(List<T> datas) {
        setEmptyView();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 刷新单条数据
     */
    @Override
    public void refreshData(int index) {
        setEmptyView();
        int position = index + mHeaderAndFooterWrapper.getHeadersCount();
        mHeaderAndFooterWrapper.notifyItemChanged(position);
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public List<T> getListDatas() {
        return mListDatas;
    }


    protected boolean isUseTouristLoadLimit() {
        return true;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        // 游客不可以加载更多；并且当前是游客；并且当前已经加载了数据了；再次下拉就触发登录
        if (isUseTouristLoadLimit() && !TouristConfig.LIST_CAN_LOAD_MORE && mPresenter.isTourist() && !mListDatas.isEmpty()) {
            hideLoading();
            showLoginPop();
            return;
        }
        mMaxId = DEFAULT_PAGE_MAX_ID;
        mPage = DEFAULT_PAGE;
        requestNetData(mMaxId, false);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        // 游客加载跟多处理
        if (isUseTouristLoadLimit() && !TouristConfig.LIST_CAN_LOAD_MORE && mPresenter.handleTouristControl()) {
            hideLoading();
            return;
        }
        mPage++;
        requestNetData(mMaxId, true);
    }

    /**
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onNetResponseSuccess(@NotNull List<T> data, boolean isLoadMore) {
        hideRefreshState(isLoadMore);
        handleReceiveData(data, isLoadMore, false);
    }

    /**
     * 处理获取到的缓存数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onCacheResponseSuccess(List<T> data, boolean isLoadMore) {
        hideRefreshState(isLoadMore);
        // 如果没有缓存，直接拉取服务器数据
        if (!isLoadMore && (data == null || data.size() == 0)) {
            getNewDataFromNet();
        } else {
            // 如果数据库有数据就先显示
            handleReceiveData(data, isLoadMore, true);
            // 如果需要刷新数据，就进行刷新，因为数据库一般都会比服务器先加载完数据，
            // 这样就能实现，数据库先加载到界面，随后刷新服务器数据的效果
            if ((!mPresenter.isTourist() || mListDatas.isEmpty()) && isNeedRefreshDataWhenComeIn()) {
                // 如果不是游客 >> 进入界面刷新， 如果是游客 >> 数据为空刷新
                getNewDataFromNet();
            }
        }
    }

    /**
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        hideRefreshState(isLoadMore);
        closeLoadingView();
        // 刷新
        if (!isLoadMore && (mListDatas.size() == 0)) {
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
            if (mHeaderAndFooterWrapper.getHeadersCount() <= 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                showMessageNotSticky(getString(R.string.err_net_not_work));
            }
        } else { // 加载更多
            showMessageNotSticky(getString(R.string.err_net_not_work));

        }
    }

    /**
     * 处理服务器或者缓存中拿到的数据
     *
     * @param data       返回的数据
     * @param isLoadMore 是否是加载更多
     */
    private void handleReceiveData(@NotNull List<T> data, boolean isLoadMore, boolean isFromCache) {
        // 刷新
        if (!isLoadMore) {
            if (isLoadingMoreEnable()) {
                mRefreshlayout.setEnableLoadmore(true);
            }
            mListDatas.clear();
            mTvNoMoredataText.setVisibility(View.GONE);
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, isLoadMore);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                mMaxId = getMaxId(data);
                refreshData();
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setErrorImag(setEmptView());
                refreshData();
                if (showEmptyViewWithNoData()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        } else { // 加载更多
            if (data != null && data.size() != 0) {
                mTvNoMoredataText.setVisibility(View.GONE);
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, isLoadMore);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                refreshData();
                mMaxId = getMaxId(data);
            }
        }
        // 数据加载后，所有的数据数量小于一页，说明没有更多数据了，就不要上拉加载了(除开缓存)
        if (!isFromCache && (data == null || data.size() < getPagesize())) {
            mRefreshlayout.setEnableLoadmore(false);
            // mListDatas.size() >= DEFAULT_ONE_PAGE_SIZE 当前数量大于一页显示数量时，显示加载更多
            if (mListDatas.size() >= DEFAULT_ONE_PAGE_SIZE || showNoMoreData()) {
                mTvNoMoredataText.setVisibility(View.VISIBLE);
            }
        }
    }

    protected boolean showEmptyViewWithNoData() {
        return mHeaderAndFooterWrapper.getHeadersCount() <= 0;
    }

    protected Long getMaxId(@NotNull List<T> data) {
        if (mListDatas.size() > 0) {
            return mListDatas.get(mListDatas.size() - 1).getMaxId();
        }
        return DEFAULT_PAGE_MAX_ID;
    }

    /**
     * 处理关闭加载、刷新状态
     *
     * @param isLoadMore
     */
    @Override
    public void hideRefreshState(boolean isLoadMore) {
        if (isLoadMore) {
            mRefreshlayout.finishLoadmore();
        } else {
            mRefreshlayout.finishRefresh();
        }
    }

    /**
     * 过度拉动了
     */
    protected void onOverScrolled() {

    }

    protected int getPagesize() {
        return DEFAULT_PAGE_SIZE;
    }

}

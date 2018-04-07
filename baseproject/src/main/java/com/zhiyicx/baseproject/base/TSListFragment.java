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
import android.view.ViewStub;
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
    public static final int DEFAULT_PAGE_SIZE = 15;

    /**
     * 一个页面显示的最大条数，用来判断是否显示加载更多
     */
    public static final int DEFAULT_ONE_PAGE_SHOW_MAX_SIZE = 12;

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
    protected TextView mTvTopTip;
    protected RecyclerView.LayoutManager layoutManager;
    private Handler mHandler;
    private Runnable mRunnable;

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

    /**
     * 没有更多数据
     */
    protected View mTvNoMoredataText;

    /**
     * 避免 Glide.resume.重复设置增加开销
     */
    private static boolean sIsScrolling;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.finishLoadmore();
        mRefreshlayout.finishRefresh();
    }

    @Override
    public void showMessage(String message) {
        hideLoading();
        showMessageNotSticky(message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    setTopTipVisible(View.GONE);
                } catch (Exception ignored) {}
            }
        };
        mRefreshlayout = (SmartRefreshLayout) rootView.findViewById(R.id.refreshlayout);
        mRvList = (RecyclerView) rootView.findViewById(R.id.swipe_target);

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
        mRvList.setItemViewCacheSize(setItemCacheSize());
        mRvList.setDrawingCacheEnabled(true);
        mRvList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //设置动画
        mRvList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = getAdapter();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooterWrapper.addFootView(getFooterView());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mRefreshlayout.setEnableAutoLoadmore(false);
        mRefreshlayout.setEnableRefresh(isRefreshEnable());
        mRefreshlayout.setEnableLoadmore(isLoadingMoreEnable());
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // SCROLL_STATE_FLING; //屏幕处于甩动状态
                // SCROLL_STATE_IDLE; //停止滑动状态
                // SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
                if (mActivity != null) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        sIsScrolling = true;
                        Glide.with(mActivity).pauseRequests();
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (sIsScrolling) {
                            if (AndroidLifecycleUtils.canLoadImage(mActivity)) {
                                Glide.with(mActivity).resumeRequests();
                            }
                        }
                        sIsScrolling = false;
                    }
                }
            }
        });
    }

    /**
     * @return recyclerVeiw item offset cache Size
     */
    protected int setItemCacheSize() {
        return 10;
    }

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
    protected void getNewDataFromNet() {
        if (isNeedRefreshAnimation() && getUserVisibleHint()) {
            mRefreshlayout.autoRefresh(100);
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
        return mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE ;
    }

    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }


    @Override
    protected void initData() {
        if (mPresenter != null) {
            if (!isLayzLoad()) {
                // 获取缓存数据
                requestCacheData(mMaxId, false);
            }
        }
    }

    protected void requestCacheData(Long maxId, boolean isLoadMore) {
        if (mPresenter != null) {
            mPresenter.requestCacheData(mMaxId, isLoadMore);
        }
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

    protected void layzLoad() {
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
        return new LinearLayoutManager(mActivity);
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
        inflateTopView();
        mTvTopTip.setText(text);
    }

    protected void setTopTipHtmlText(@NotNull String text) {
        inflateTopView();
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
        inflateTopView();
        mTvTopTip.setVisibility(visibility);
    }

    /**
     * 懒加载 top Tip
     */
    private void inflateTopView() {
        if (mTvTopTip == null) {
            ViewStub mTopTipStub = (ViewStub) mRootView.findViewById(R.id.stub_toptip);
            mTvTopTip = (TextView) mTopTipStub.inflate();
            RxView.clicks(mTvTopTip)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            onTopTipClick();
                        }
                    });
        }
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
        mHandler.postDelayed(mRunnable, DEFAULT_TIP_STICKY_TIME);
    }

    protected void requestNetData(Long maxId, boolean isLoadMore) {
        if (mPresenter != null) {
            mPresenter.requestNetData(maxId, isLoadMore);
        }
    }

    /**
     * 设置 emptyview 可见性
     *
     * @param visiable true 可见
     */
    public void setEmptyViewVisiable(boolean visiable) {
        layzLoadEmptyView();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visiable ? View.VISIBLE : View.GONE);
        }
    }

    protected void layzLoadEmptyView() {
        if (mEmptyView == null) {
            try {
                ViewStub viewStub = (ViewStub) mRootView.findViewById(R.id.stub_empty_view);
                mEmptyView = (EmptyView) viewStub.inflate();
//                mEmptyView = (EmptyView) mRootView.findViewById(R.id.empty_view);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData() {
        if (mHeaderAndFooterWrapper != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0);
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData(List<T> datas) {
        setEmptyViewVisiable(mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 刷新单条数据
     */
    @Override
    public void refreshData(int index) {
        setEmptyViewVisiable(mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0);
        int position = index + mHeaderAndFooterWrapper.getHeadersCount();
        mHeaderAndFooterWrapper.notifyItemChanged(position);
    }

    @Override
    public void refreshRangeData(int start, int count) {
        if (mHeaderAndFooterWrapper != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0);
            int position = start + mHeaderAndFooterWrapper.getHeadersCount();
            mHeaderAndFooterWrapper.notifyItemRangeChanged(position, count);
        }
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
     * 手动刷新
     */
    @Override
    public void startRefrsh() {
        if (mRefreshlayout != null) {
            mRvList.scrollToPosition(0);
            mRefreshlayout.autoRefresh(10);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        try {
            mHandler.removeCallbacks(mRunnable);
            setTopTipVisible(View.GONE);
        } catch (Exception ignored) {}
        // 游客不可以加载更多；并且当前是游客；并且当前已经加载了数据了；再次下拉就触发登录
        if (isUseTouristLoadLimit() && !TouristConfig.LIST_CAN_LOAD_MORE && mPresenter != null && mPresenter.isTourist() && !mListDatas.isEmpty()) {
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
            layzLoadEmptyView();
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
            if (mHeaderAndFooterWrapper.getHeadersCount() <= 0) {
                setEmptyViewVisiable(true);
            } else {
                setEmptyViewVisiable(false);
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
    protected void handleReceiveData(List<T> data, boolean isLoadMore, boolean isFromCache) {
        // 刷新
        if (!isLoadMore) {

            mTvNoMoredataText.setVisibility(View.GONE);
            if (isLoadingMoreEnable()) {
                mRefreshlayout.setEnableLoadmore(true);
            }
            mListDatas.clear();
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, false);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                mMaxId = getMaxId(data);
                refreshData();

            } else {
                layzLoadEmptyView();
                mEmptyView.setErrorImag(setEmptView());
                refreshData();
            }
        } else { // 加载更多
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, true);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                try {
                    refreshRangeData(mListDatas.size() - data.size() - 1, data.size());
                } catch (Exception e) {
                    refreshData();
                }
                mMaxId = getMaxId(data);
            }
        }
        // 数据加载后，所有的数据数量小于一页，说明没有更多数据了，就不要上拉加载了(除开缓存)
        if (!isFromCache && (data == null || data.size() < getPagesize())) {
            mRefreshlayout.setEnableLoadmore(false);
            // mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE 当前数量大于一页显示数量时，显示加载更多
            if (showNoMoreData()) {
                mTvNoMoredataText.setVisibility(View.VISIBLE);
            }
        }
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
//            mRefreshlayout.finishLoadmore();
            mRefreshlayout.finishLoadmore(50);
        } else {
            mRefreshlayout.finishRefresh();
        }
    }

    /**
     * 默认加载条数，具体数据又后端确定
     * @return
     */
    protected int getPagesize() {
        return DEFAULT_PAGE_SIZE;
    }


}

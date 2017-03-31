package com.zhiyicx.baseproject.base;


import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/2/7
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends ITSListPresenter<T>, T extends BaseListBean> extends TSFragment<P> implements OnRefreshListener, OnLoadMoreListener, ITSListView<T, P> {
    public static final int DEFAULT_PAGE_SIZE = 20; // 默认每页的数量

    public static final Long DEFAULT_PAGE_MAX_ID = 0L;// 默认初始化列表 id
    public static final int DEFAULT_PAGE = 1;// 默认初始化列表分页，只对当 max_id 无法使用时有效，如热门动态

    private static final int DEFAULT_TIP_STICKY_TIME = 3000;
    private static final float DEFAULT_LIST_ITEM_SPACING = 0.5f;

    private static final boolean DEFAULT_NEED_REFRESH = false;

    protected List<T> mListDatas = new ArrayList<>();

    protected RecyclerView.Adapter mAdapter;

    protected EmptyWrapper mEmptyWrapper;
    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View mFooterView;

    protected SwipeToLoadLayout mRefreshlayout;

    protected RecyclerView mRvList;

    protected View mFlTopTipContainer;
    protected TextView mTvTopTip;
    protected RecyclerView.LayoutManager layoutManager;


    protected EmptyView mEmptyView;

    // 当前数据加载状态
    protected int mEmptyState = EmptyView.STATE_DEFAULT;

    protected Long mMaxId = DEFAULT_PAGE_MAX_ID; // 纪录当前列表 item id 最大值，用于分页

    protected int mPage = DEFAULT_PAGE;// 只对当 max_id 无法使用时有效，如热门动态

    private boolean mIsTipMessageSticky;// 提示信息是否需要常驻
    private View mTvNoMoredataText;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
        mRefreshlayout.setLoadingMore(false);
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mRefreshlayout = (SwipeToLoadLayout) rootView.findViewById(R.id.refreshlayout);
        mRvList = (RecyclerView) rootView.findViewById(R.id.swipe_target);
        mFlTopTipContainer = rootView.findViewById(R.id.fl_top_tip_container);
        RxView.clicks(mFlTopTipContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onTopTipClick();
                    }
                });
        mTvTopTip = (TextView) rootView.findViewById(R.id.tv_top_tip_text);
        mEmptyView = (EmptyView) rootView.findViewById(R.id.empty_view);
//        mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mEmptyView.setErrorImag(setEmptView());
        mEmptyView.setNeedTextTip(false);
        mEmptyView.setNeedClickLoadState(false);
        RxView.clicks(mEmptyView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mRefreshlayout.setRefreshing(true);
                    }
                });
        mRefreshlayout.setOnRefreshListener(this);
        mRefreshlayout.setOnLoadMoreListener(this);
        if (setListBackColor() != -1) {
            mRvList.setBackgroundColor(getResources().getColor(setListBackColor()));
        }
        layoutManager = getLayoutManager();
        mRvList.setLayoutManager(layoutManager);
        mRvList.addItemDecoration(getItemDecoration());//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList.setHasFixedSize(sethasFixedSize());
        mRvList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mAdapter = getAdapter();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooterWrapper.addFootView(getFooterView());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
//        mEmptyWrapper = new EmptyWrapper(mHeaderAndFooterWrapper);
//        mEmptyWrapper.setEmptyView(mEmptyView);
//        mRvList.setAdapter(mEmptyWrapper);
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

    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }


    @Override
    protected void initData() {
        mRefreshlayout.setRefreshEnabled(isRefreshEnable());
        mRefreshlayout.setLoadMoreEnabled(isLoadingMoreEnable());
        onCacheResponseSuccess(requestCacheData(mMaxId, false), false); // 获取缓存数据
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
     * 是否需要下拉加载
     *
     * @return true 需要
     */
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    protected boolean getIsShowLoadingMore() {
        return false;
    }

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
        if (mIsTipMessageSticky) {// 如果有常驻信息在，忽略此条提示
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

    protected List<T> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore);
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData() {
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData(List<T> datas) {
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 刷新单条数据
     */
    @Override
    public void refreshData(int index) {
        mHeaderAndFooterWrapper.notifyItemChanged(index);
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public List<T> getListDatas() {
        return mListDatas;
    }

    @Override
    public void onRefresh() {
        mMaxId = DEFAULT_PAGE_MAX_ID;
        mPage = DEFAULT_PAGE;
        requestNetData(mMaxId, false);
    }

    @Override
    public void onLoadMore() {
        if (mMaxId == null || mMaxId == 0) {
            mRefreshlayout.setLoadingMore(false);
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
        handleRefreshState(isLoadMore);
        handleReceiveData(data, isLoadMore, false);
    }

    /**
     * 处理获取到的缓存数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onCacheResponseSuccess(@NotNull List<T> data, boolean isLoadMore) {
        handleRefreshState(isLoadMore);
        if (!isLoadMore && (data == null || data.size() == 0)) {// 如果没有缓存，直接拉取服务器数据
            mRefreshlayout.setRefreshing(true);
        } else {
            // 如果数据库有数据就先显示
            handleReceiveData(data, isLoadMore, true);
            // 如果需要刷新数据，就进行刷新，因为数据库一般都会比服务器先加载完数据，
            // 这样就能实现，数据库先加载到界面，随后刷新服务器数据的效果
            if (isNeedRefreshDataWhenComeIn()) {
                mRefreshlayout.setRefreshing(true);
            }
        }
    }

    /**
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        handleRefreshState(isLoadMore);
        if (!isLoadMore && (mListDatas.size() == 0)) { // 刷新
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
            mEmptyView.setVisibility(View.VISIBLE);
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
        if (!isLoadMore) { // 刷新
            if (isLoadingMoreEnable()) {
                mRefreshlayout.setLoadMoreEnabled(true);
            }
            mListDatas.clear();
            mTvNoMoredataText.setVisibility(View.GONE);
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                mMaxId = getMaxId(data);
                refreshData();
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setErrorImag(setEmptView());
                refreshData();
                mEmptyView.setVisibility(View.VISIBLE);
            }


        } else { // 加载更多
            if (data != null && data.size() != 0) {
                mTvNoMoredataText.setVisibility(View.GONE);
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                refreshData();
                mMaxId = getMaxId(data);
            } else {
                mRefreshlayout.setLoadMoreEnabled(false);
                if (mListDatas.size() >= DEFAULT_PAGE_SIZE) {
                    mTvNoMoredataText.setVisibility(View.VISIBLE);
                    mRvList.smoothScrollToPosition(mListDatas.size() - 1);
                }
            }
        }
        // 数据加载后，所有的数据数量小于一页，说明没有更多数据了，就不要上拉加载了
        if (mListDatas.size() < DEFAULT_PAGE_SIZE) {
            mRefreshlayout.setLoadMoreEnabled(false);
        }
    }

    protected Long getMaxId(@NotNull List<T> data) {
        return data.get(data.size() - 1).getMaxId();
    }

    /**
     * 处理关闭加载、刷新状态
     *
     * @param isLoadMore
     */
    private void handleRefreshState(boolean isLoadMore) {
        if (isLoadMore) {
            mRefreshlayout.setLoadingMore(false);
        } else {
            mRefreshlayout.setRefreshing(false);
        }
    }
}

package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.LiveItemModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.RequestErroException;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.LivePlayActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.FollowStreamListAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.LiveListAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.VideoListAdapter;
import com.zhiyicx.zhibolibrary.ui.fragment.LiveItemFragment;
import com.zhiyicx.zhibolibrary.ui.view.LiveItemView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public class LiveItemPresenter extends BasePresenter<LiveItemModel, LiveItemView> {
    private int mPage;
    private ArrayList<SearchResult> mListDatas;
    private ArrayList<SearchResult> mStreamListDatas = new ArrayList<>();
    private ArrayList<SearchResult> mNotStreamListDatas = new ArrayList<>();


    private ApiList mApiList;
    private MoreAdapter mAdapter;
    private MoreLinearAdapter mLinearAdapter;
    public static final int LIVE_PAGE = 0;
    public static final int VIDEO_PAGE = 1;
    public int mCurrentPage;

    private int PAGE_LIMIT = ListBasePresenter.PAGE_LIMIT;
    private Subscription mFilterSubscription;
    private Subscription mNotFilterSubscription;
    private Subscription mUsidSubscription;
    private SearchResult mData;
    private String mIconUrl;

    @Inject
    @ActivityScope
    public LiveItemPresenter(LiveItemModel model, LiveItemView rootView) {
        super(model, rootView);
        if (mRootView.getOrder().equals("video")) {
            mCurrentPage = VIDEO_PAGE;
        }
        else {
            mCurrentPage = LIVE_PAGE;
        }
    }


    /**
     * 拉取列表
     *
     * @param isMore 是否加载更多
     */
    public void getList(boolean isMore) {
        if (!isMore) {
            mPage = 1;//如果刷新,PAGE默认为1
        }
        Log.w(TAG, "isFliter ? " + mRootView.isFilter());
        initAdapter();//初始化adapter
        if (!TextUtils.isEmpty(mRootView.getUsid())) {//说明是直接获取指定用户回放列表的fragment
            getUserList(isMore);
            return;
        }
        if (ZhiboApplication.userInfo == null) ZhiboApplication.getUserInfo();//此处解决经常报空的问题
        if (mRootView.isFilter()) {//筛选
            filter(isMore);
        }
        else {//不筛选
            notFilter(isMore);
        }
    }

    /**
     * 筛选列表
     *
     * @param isMore
     */
    private void filter(final boolean isMore) {
        mFilterSubscription = mModel.getFlterList(
                mRootView.getOrder()
                , mRootView.getVideoOreder()
                , mPage, mRootView.getFilterValue())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isMore) {
                            mRootView.showRefreshing();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiList>() {
                    @Override
                    public void call(ApiList ApiList) {
                        if (ApiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mApiList = ApiList;
                            filterRefresh(isMore);
                        }
                        else {
                            mRootView.setIsFilter(false);
                            mRootView.showMessage(ApiList.message);
                            filterRefresh(isMore);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (throwable instanceof RequestErroException) {
                            mRootView.showMessage(throwable.getMessage());
                        }
                        mRootView.setIsFilter(false);
                        if (!isMore) {//隐藏loading
                            mRootView.hideRefreshing();
                            loadForNetBad();
                        }
                        else {
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        }
                    }
                });
    }

    private void filterRefresh(final boolean isMore) {
        if (mApiList == null || mApiList.data == null || mApiList.data == null) {
            refresh(mApiList, isMore);//刷新数据
            return;
        }
        String usids = "";
        for (SearchResult data : mApiList.data) {
            usids += data.user.usid + ",";
        }
        if (usids.length() > 0)
            usids = usids.substring(0, usids.length() - 1);
        if (usids.length() > 0) {
            mUsidSubscription=   mModel.getUsidInfo(usids, "").observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseJson<UserInfo[]>>() {
                @Override
                public void call(BaseJson<UserInfo[]> baseJson) {
                    if (baseJson.code.equals(ZBLApi.REQUEST_SUCESS)) {
                        UserInfo[] userInfos = baseJson.data;
                        for (int i = 0; i < mApiList.data.length; i++) {
                            mApiList.data[i].user = userInfos[i];
                        }
                        refresh(mApiList, isMore);//刷新数据
                        if (mCurrentPage == VIDEO_PAGE) {//区分处理两个子类的筛选按钮
                            EventBus.getDefault().post(true, "set_filter_satus_replay");
                        }
                        else {
                            EventBus.getDefault().post(true, "set_filter_satus_live");
                        }


                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        else {
            refresh(mApiList, isMore);//刷新数据
            if (mCurrentPage == VIDEO_PAGE) {//区分处理两个子类的筛选按钮
                EventBus.getDefault().post(true, "set_filter_satus_replay");
            }
            else {
                EventBus.getDefault().post(true, "set_filter_satus_live");
            }

        }

    }


    /**
     * 加载时遇到网络状况不佳
     */
    protected void loadForNetBad() {
        if (mListDatas != null && mListDatas.size() > 0) {
            //如果列表有数据则清空
            mListDatas.clear();
            if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW))
                mLinearAdapter.notifyDataSetChanged();
            else
                mAdapter.notifyDataSetChanged();//通知页面更新数据
        }
        mRootView.showNetBadPH();

    }

    /**
     * 不筛选列表
     *
     * @param isMore
     */
    private void notFilter(final boolean isMore) {
        Observable<ApiList> observable = mModel.getNotList(
                mRootView.getOrder()
                , mRootView.getVideoOreder()
                , mPage);
        notFilterWrap(isMore, observable);
    }

    /**
     * 获取指定用户的回放列表
     *
     * @param isMore
     */
    private void getUserList(final boolean isMore) {
        // TODO: 16/8/4 usid
        Observable<ApiList> observable = mModel.getUserList(
                mPage
                , mRootView.getUsid()).subscribeOn(Schedulers.io()).map(new Func1<ApiList, ApiList>() {
            @Override
            public ApiList call(ApiList apiList) {

                return apiList;
            }
        });
        notFilterWrap(isMore, observable);
    }

    private void notFilterWrap(final boolean isMore, Observable<ApiList> observable) {
        mNotFilterSubscription = observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isMore) {
                            mRootView.showRefreshing();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<ApiList>() {
                    @Override
                    public void call(ApiList ApiList) {
                        if (ApiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mApiList = ApiList;
                            notFilterWrapRefresh(isMore);

                        }
                        else {
                            mRootView.showMessage(ApiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (!isMore) {//隐藏loading
                            mRootView.hideRefreshing();
                            loadForNetBad();
                        }
                        else {
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        }
                    }
                });
    }

    private void notFilterWrapRefresh(final boolean isMore) {

        if (mApiList == null || mApiList.data == null || mApiList.data.length == 0) {
            refresh(mApiList, isMore);//刷新数据
            return;
        }
        String usids = "";
        for (SearchResult data : mApiList.data) {
            usids += data.user.usid + ",";
        }
        if (usids.length() > 0)
            usids = usids.substring(0, usids.length() - 1);
        if (usids.length() > 0) {

            mUsidSubscription=    mModel.getUsidInfo(usids, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseJson<UserInfo[]>>() {
                @Override
                public void call(BaseJson<UserInfo[]> baseJson) {
                    if (baseJson.code.equals(ZBLApi.REQUEST_SUCESS)) {
                        UserInfo[] userInfos = baseJson.data;
                        for (int i = 0; i < mApiList.data.length; i++) {
                            mApiList.data[i].user = userInfos[i];
                        }
                        refresh(mApiList, isMore);//刷新数据

                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        else {

            refresh(mApiList, isMore);//刷新数据
        }
    }

    /**
     * 刷新数据
     *
     * @param ApiList
     * @param isMore
     */
    private void refresh(ApiList ApiList, boolean isMore) {
        if (!isMore) {
            try {
                mRootView.hideRefreshing();
            } catch (Exception e) {

            }

        }
        ++mPage;
        if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
            mLinearAdapter.isShowFooter(true);
        }
        else
            mAdapter.isShowFooter(true);
        if (ApiList == null || ApiList.data == null || ApiList.data.length == 0) {//没有数据
            hideMoreLoading();
            if (!isMore) {//如果是上拉刷新，没有数据则清理以前的数据
                mListDatas.clear();
                mStreamListDatas.clear();
                mNotStreamListDatas.clear();
                if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW))
                    mLinearAdapter.notifyDataSetChanged();//通知页面更新数据
                else
                    mAdapter.notifyDataSetChanged();//通知页面更新数据
            }
            if (!isMore) {//上拉刷新为空提示用户
                if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
//                    mRootView.showMessage(ApiList.message + "~");
                    mRootView.showNotFollowPH();
                }
                else if (mRootView.isFilter()) {//筛选的时候没有数据
                    mRootView.showFilterNothingPH();
                }
                else {
                    mRootView.shwoNothingPH();
//                    if (mCurrentPage == VIDEO_PAGE) {
//                        mRootView.showMessage("他/她还没有录制过视频哦~`");
//                    } else {
//                        mRootView.showMessage("还没有人直播哦~");
//                    }
                }
            }
            else {
                mRootView.showMessage(UiUtils.getString("str_load_more_prompt"));//没有数据提示用户
            }
            return;
        }

        if (!isMore) {
            mRootView.hidePlaceHolder();
            mListDatas.clear();//如果是上拉刷新，清理之前的数据
            mStreamListDatas.clear();
            mNotStreamListDatas.clear();
            if (ApiList.data.length < PAGE_LIMIT) {//第一次数据少于最大数量则不能加载更多
                hideMoreLoading();
            }
        }
        if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
            /**
             * 处理直播排序数据，在直播的排在前面
             */
            for (SearchResult data : ApiList.data) {//添加数据
                if (data.stream != null)
                    mStreamListDatas.add(data);//在直播
                else
                    mNotStreamListDatas.add(data);
            }
            mListDatas.clear();
            mListDatas.addAll(mStreamListDatas);
            mListDatas.addAll(mNotStreamListDatas);
        }
        else {
            for (SearchResult data : ApiList.data) {//添加数据
                mListDatas.add(data);
            }
        }
        if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW))
            mLinearAdapter.notifyDataSetChanged();//通知页面更新数据
        else
            mAdapter.notifyDataSetChanged();//通知页面更新数据
    }

    private void hideMoreLoading() {
        if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
            mLinearAdapter.isShowFooter(false);
            mLinearAdapter.notifyDataSetChanged();
        }
        else {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mListDatas == null) {//如果list为空说明第一次拉取列表
            mListDatas = new ArrayList<SearchResult>();
            if (mCurrentPage == VIDEO_PAGE) {//回放的adapter
                mAdapter = new VideoListAdapter(mListDatas);
                mRootView.setAdapter(mAdapter);
            }
            else {//直播列表的adapter
                if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
                    mLinearAdapter = new FollowStreamListAdapter(mListDatas);
                    mRootView.setMoreLineAdapter(mLinearAdapter);
                }
                else {
                    mAdapter = new LiveListAdapter(mListDatas);
                    mRootView.setAdapter(mAdapter);
                }
            }
            initItemListener();
        }
    }

    /**
     * 初始化Item点击事件
     */
    private void initItemListener() {
        if (mRootView.getOrder().equals(LiveItemFragment.TYPE_FOLLOW)) {
            mLinearAdapter.setOnItemClickListener(new MoreLinearAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
                @Override
                public void onItemClick(View view, SearchResult data) {
                    mData = data;

                    if (mCurrentPage == VIDEO_PAGE) {
                        UserInfo userInfo = new UserInfo();
                        mIconUrl = data.video.video_icon.getOrigin();
                        try {
                            userInfo = data.user;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            userInfo.location = data.video.video_location;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startVideo(data.video.vid, userInfo);
                    }
                    else {
                        if (mData.stream != null) {
                            startPlay(mData);
                        }
                        else {
                            mRootView.showMessage(UiUtils.getString("str_not_online_prompt"));//没有数据提示用户
                        }
                    }
                }
            });
        }
        else {
            mAdapter.setOnItemClickListener(new MoreAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
                @Override
                public void onItemClick(View view, SearchResult data) {
                    mData = data;

                    if (mCurrentPage == VIDEO_PAGE) {
                        UserInfo userInfo = new UserInfo();
                        mIconUrl = data.video.video_icon.getOrigin();
                        try {
                            userInfo= data.user;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            userInfo.location = data.video.video_location;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startVideo(data.video.vid, userInfo);
                    }
                    else {
                        if (mData.stream != null) {
                            startPlay(mData);
                        }
                        else {
                            mRootView.showMessage(UiUtils.getString("str_not_online_prompt"));//没有数据提示用户
                        }
                    }
                }
            });
        }
    }

    private void startVideo(String vid, UserInfo userInfo) {
        Intent intent = new Intent(UiUtils.getContext(), LivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        bundle.putString("vid", vid);
        bundle.putString("iconUrl", mIconUrl);
        bundle.putBoolean("isVideo", true);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }

    private void startPlay(SearchResult mData) {
        Intent intent = new Intent(UiUtils.getContext(), LivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mData);
        bundle.putBoolean("isVideo", false);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    /**
     * 播放回放
     *
     * @param http 播放地址
     */
    private void launchVideo(String http, UserInfo userInfo) {
        Intent intent = new Intent(UiUtils.getContext(), LivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", http);//加密streamid
        bundle.putSerializable("userInfo", userInfo);
        bundle.putString("iconUrl", mIconUrl);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    /**
     * 播放回放
     *
     * @param http 播放地址
     */
    private void launchVideo(String http) {
        Intent intent = new Intent(UiUtils.getContext(), LivePlayActivity.class);
        intent.putExtra("url", http);//加密streamid
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mData);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mFilterSubscription);//解除订阅
        unSubscribe(mNotFilterSubscription);//解除订阅
        unSubscribe(mUsidSubscription);//解除订阅
    }
}

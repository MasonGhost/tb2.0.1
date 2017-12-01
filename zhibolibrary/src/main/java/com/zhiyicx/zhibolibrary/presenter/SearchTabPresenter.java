package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.SearchTabModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLLivePlayActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SearchListAdapter;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLSearchTabFragement;
import com.zhiyicx.zhibolibrary.ui.holder.SearchListHolder;
import com.zhiyicx.zhibolibrary.ui.view.SearchTabView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/6.
 */
@ActivityScope
public class SearchTabPresenter extends BasePresenter<SearchTabModel, SearchTabView> {
    private int mPage;
    private ArrayList<SearchResult> mListDatas;
    private SearchListAdapter mAdapter;
    public static final int SEARCH_USER = 0;
    public static final int SEARCH_STREAM = 1;
    public static final int SEARCH_VIDEO = 2;
    private int currentSearch;

    public int PAGE_LIMIT = ListBasePresenter.PAGE_LIMIT;
    public final String TAG = getClass().getSimpleName();
    private String mIconUrl;
    private UserInfo mUserInfo;
    private SearchJson mSearchJson;
    private Subscription mUserInfoSubscription;
    private Subscription mSearchSubscription;


    @Inject
    public SearchTabPresenter(SearchTabModel model, SearchTabView rootView) {
        super(model, rootView);
    }

    /**
     * 获取搜索Observable
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    private Observable<SearchJson> getSearchObservable(String keyword) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("p", mPage);
        map.put("keyword", keyword);
        switch (mRootView.getType()) {
            case "user":
                return mModel.Search(ZhiboApplication.getUserInfo().auth_accesskey
                        , ZhiboApplication.getUserInfo().auth_secretkey
                        , keyword
                        , mPage)
                        .subscribeOn(Schedulers.io());
            case "video":
                return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_SEARCH_VIDEO_LIST, map).map(new Func1<JsonObject,
                        SearchJson>() {
                    @Override
                    public SearchJson call(JsonObject jsonObject) {
                        return new Gson().fromJson(jsonObject, SearchJson.class);
                    }
                });
            case "stream":
            default:
                return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_SEARCH_LIVE_LIST, map).map(new Func1<JsonObject,
                        SearchJson>() {
                    @Override
                    public SearchJson call(JsonObject jsonObject) {
                        return new Gson().fromJson(jsonObject, SearchJson.class);
                    }
                });

        }
    }

    /**
     * 搜索
     *
     * @param keyword
     * @param isMore
     */
    public void search(String keyword, final boolean isMore) {
        if (!isMore) {
            mPage = 1;//如果刷新,PAGE默认为1
        }
        if (keyword.equals(ZBLSearchTabFragement.DEFAULT_KEYWORD))
            keyword = "";
        initAdapter();//初始化adapter
        try {
            mSearchSubscription = getSearchObservable(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (!isMore && mRootView != null) {
                                mRootView.showRefreshing();
                            }
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .finallyDo(new Action0() {
                        @Override
                        public void call() {
                            if (!isMore && mRootView != null) {
                                mRootView.hideRefreshing();
                            }
                        }
                    })
                    .subscribe(new Action1<SearchJson>() {
                        @Override
                        public void call(SearchJson json) {
                            if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                                mSearchJson = json;
                                if (json.data.user_list == null) {
                                    /**
                                     * 通过usid获取用户信息
                                     */
                                    String usid = "";
                                    if (json.data.stream_list != null) {
                                        for (SearchResult searchResult : json.data.stream_list) {
                                            usid += searchResult.user.usid + ",";
                                        }
                                    } else {
                                        for (SearchResult searchResult : json.data.video_list) {
                                            usid += searchResult.user.usid + ",";
                                        }
                                    }
                                    if (usid.length() > 0)
                                        usid = usid.substring(0, usid.length() - 1);
                                    if (usid.length() > 0) {
                                        mUserInfoSubscription = mModel.getUsidInfo(usid, "")
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .map(new Func1<BaseJson<UserInfo[]>, SearchJson>() {
                                                    @Override
                                                    public SearchJson call(BaseJson<UserInfo[]> baseJson) {
                                                        if (baseJson.code.equals(ZBLApi.REQUEST_SUCESS)) {
                                                            UserInfo[] userInfos = baseJson.data;
                                                            HashMap<String, UserInfo> userInfoHashMap = new HashMap<>();
                                                            if (userInfos != null && userInfos.length > 0) {
                                                                for (int i = 0; i < userInfos.length; i++) {
                                                                    userInfoHashMap.put(userInfos[i].usid, userInfos[i]);
                                                                }
                                                                if (mSearchJson.data.stream_list != null) {
                                                                    for (SearchResult datum : mSearchJson.data.stream_list) {
                                                                        datum.user = userInfoHashMap.get(datum.user.usid);
                                                                    }
                                                                } else {
                                                                    for (SearchResult datum : mSearchJson.data.video_list) {
                                                                        datum.user = userInfoHashMap.get(datum.user.usid);
                                                                    }

                                                                }

                                                            }
                                                        } else {
                                                            return null;
                                                        }
                                                        return mSearchJson;
                                                    }
                                                })
                                                .subscribe(new Action1<SearchJson>() {
                                                    @Override
                                                    public void call(SearchJson data) {
                                                        if (data != null) {
                                                            refresh(mSearchJson, isMore);//刷新数据
                                                        } else {
                                                            errorDeal(isMore);
                                                        }
                                                    }
                                                }, new Action1<Throwable>()

                                                {
                                                    @Override
                                                    public void call(Throwable throwable) {
                                                        throwable.printStackTrace();
                                                        errorDeal(isMore);
                                                    }
                                                });

                                    } else {
                                        refresh(json, isMore);//刷新数据
                                    }
                                } else {
                                    refresh(json, isMore);//刷新数据
                                }

                            } else {
                                mRootView.showMessage(json.message);
                            }
                        }
                    }, new Action1<Throwable>()

                    {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            try {
                                if (!isMore) {//隐藏loading
                                    mRootView.hideRefreshing();
                                    loadForNetBad();
                                } else {
                                    mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            if (!isMore) {//隐藏loading
                mRootView.hideRefreshing();
                loadForNetBad();
            } else {
                mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
            }
        }
    }

    private void errorDeal(boolean isMore) {
        if (!isMore) {//隐藏loading
            mRootView.hideRefreshing();
            if (mListDatas != null && mAdapter != null) {
                mListDatas.clear();
                mAdapter.notifyDataSetChanged();//通知页面更新数据
                mRootView.showPlaceHolder();
            }
        } else {
            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
        }
    }

    /**
     * 加载时遇到网络状况不佳
     */
    protected void loadForNetBad() {
        if (mListDatas != null && mListDatas.size() > 0) {
            //如果列表有数据则清空
            mListDatas.clear();
            mAdapter.notifyDataSetChanged();//通知页面更新数据
        }
        mRootView.showNetBadPH();

    }

    /**
     * 刷新数据
     *
     * @param isMore
     */
    private void refresh(SearchJson json, boolean isMore) {
        ++mPage;
        mAdapter.isShowFooter(true);
        SearchResult[] results = getResults(json.data);
        if (results == null || results.length == 0) {//没有数据
            hideMoreLoading();
            if (!isMore) {//如果是上拉刷新，没有数据则清理以前的数据
                mListDatas.clear();
                mAdapter.notifyDataSetChanged();//通知页面更新数据
//                mRootView.showMessage("搜索不到信息~");
                mRootView.showPlaceHolder();
            } else {
                mRootView.showMessage(UiUtils.getString("str_load_more_prompt"));
            }
            return;
        }

        if (!isMore) {
            mRootView.hidePlaceHolder();
            mListDatas.clear();//如果是上拉刷新，清理之前的数据
            if (results.length < PAGE_LIMIT) {//第一次数据少于最大数量则不能加载更多
                hideMoreLoading();
            }
        }
        for (SearchResult data : results) {//添加数据
            mListDatas.add(data);
        }
        mAdapter.notifyDataSetChanged();//通知页面更新数据
    }

    private void hideMoreLoading() {
        mAdapter.isShowFooter(false);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 根据type获得结果数组
     *
     * @param data
     * @return
     */
    private SearchResult[] getResults(SearchJson.AllSearch data) {
        String type = mRootView.getType();
        if (type == null) return null;
        if (type.equals("user")) {
            currentSearch = SEARCH_USER;
            return data.user_list;
        } else if (type.equals("stream")) {
            currentSearch = SEARCH_STREAM;
            return data.stream_list;
        } else if (type.equals("video")) {
            currentSearch = SEARCH_VIDEO;
            return data.video_list;
        } else {
            return null;
        }
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mListDatas == null) {//如果list为空说明第一次拉取列表
            mListDatas = new ArrayList<>();
            mAdapter = new SearchListAdapter(mListDatas);
            mRootView.setAdapter(mAdapter);
            initItemListener();
        }
    }

    /**
     * 初始化Item点击事件
     */
    private void initItemListener() {
        mAdapter.setOnItemClickListener(new MoreLinearAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(View view, SearchResult data) {
                dispatchAction(data);
            }
        });
    }

    /**
     * 根据不同的搜索项分配不同的点击动作
     *
     * @param data
     */
    private void dispatchAction(SearchResult data) {
        watchUser(data);
    }

    @Subscriber(tag = SearchListHolder.DISPATCH_EVENT_VIDEO, mode = ThreadMode.MAIN)
    public void handleSearchItemClickEvent(SearchResult data) {
        mUserInfo = data.user;//用户信息
        if (data.stream != null) {
            if (mUserInfo != null)
                mUserInfo.location = data.stream.location;
            startPlay(data.stream.id);
            mIconUrl = data.stream.icon.getOrigin();//获得封面

        } else if (data.video != null) {
            if (mUserInfo != null)
                mUserInfo.location = data.video.video_location;
            startVideo(data.video.vid);
            mIconUrl = data.video.video_icon.getOrigin();//获得封面
        }

    }

    /**
     * 查看用户主页
     *
     * @param data
     */
    public void watchUser(SearchResult data) {
        // TODO: 16/10/10 跳转到用户信息
//        Intent intent = new Intent(UiUtils.getContext(), UserHomeActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("user_info", data);
//        intent.putExtras(bundle);
//        mRootView.launchActivity(intent);
    }

    /**
     * 开始播放
     *
     * @param streamId
     */
    private void startPlay(String streamId) {
        if (mUserInfo == null) {
            mRootView.showMessage(UiUtils.getString("str_get_userinfo_fail"));
            return;
        }

        Intent intent = new Intent(UiUtils.getContext(), ZBLLivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("iconUrl", mIconUrl);
        bundle.putSerializable("userInfo", mUserInfo);
        bundle.putString("sid", streamId);
        bundle.putBoolean("isVideo", false);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    /**
     * 开始播放回放
     *
     * @param vid video id
     */
    private void startVideo(String vid) {
        if (mUserInfo == null) {
            mRootView.showMessage(UiUtils.getString("str_get_userinfo_fail"));
            return;
        }
        Intent intent = new Intent(UiUtils.getContext(), ZBLLivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", mUserInfo);
        bundle.putString("iconUrl", mIconUrl);
        bundle.putString("vid", vid);
        bundle.putBoolean("isVideo", true);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    @Override
    public void onDestroy() {
        unSubscribe(mSearchSubscription);
        unSubscribe(mUserInfoSubscription);
        super.onDestroy();

    }
}

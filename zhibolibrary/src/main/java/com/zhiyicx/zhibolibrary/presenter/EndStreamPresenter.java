package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.app.policy.SharePolicy;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.EndStreamModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.ShareContent;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.LivePlayActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.DefaultAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.RecommendListAdapter;
import com.zhiyicx.zhibolibrary.ui.view.EndStreamView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/5.
 */
@ActivityScope
public class EndStreamPresenter extends BasePresenter<EndStreamModel, EndStreamView> {
    private ArrayList<SearchResult> mSearchResults;
    private RecommendListAdapter mAdapter;
    private Subscription mQuerySubscribe;
    private String mUserId;
    private SharePolicy mSharePolicy;
    private UserInfo mPresenterInfo;

    @Inject
    public EndStreamPresenter(EndStreamModel model, EndStreamView rootView, SharePolicy sharePolicy) {
        super(model, rootView);
        this.mSharePolicy = sharePolicy;
    }


    public void getIntent(Intent intent) {
        boolean isAudience = intent.getBooleanExtra("isAudience", true);
        mUserId = intent.getStringExtra("userId");
        if (isAudience) {//是观众
            mRootView.isAudience(true);
            Bundle bundle = intent.getExtras();
            try {
                SearchResult[] info = ((ApiList) bundle.getSerializable("info")).data;
                mPresenterInfo = (UserInfo) bundle.getSerializable("presenter");
                requestSearchResult(bundle);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            initAudienceLayout();//初始化观众结束界面


        }
        else {//是主播
            mRootView.isAudience(false);
            Bundle bundle = intent.getExtras();
            ZBEndStreamJson.InCome income = (ZBEndStreamJson.InCome) bundle.getSerializable("income");
            mPresenterInfo = (UserInfo) bundle.getSerializable("presenter");
            //因为重连失败弹出直播间显示异常提示
            if (intent.getBooleanExtra("isException", false)) mRootView.showExceptionPrompt(true);
            mRootView.setFans(income.view_count + "");
            mRootView.setStar(income.zan_count + "");
            mRootView.setGold(income.gold + "");
        }
        mSharePolicy.setShareContent(ShareContent.getShareContentByUserInfo(mPresenterInfo));
    }


    /**
     * 分享朋友圈
     */
    public void shareMoment() {
        mSharePolicy.shareMoment();
    }


    /**
     * 分享微信
     */
    public void shareWechat() {
        mSharePolicy.shareWechat();
    }

    /**
     * 分享微博
     */
    public void shareWeibo() {
        mSharePolicy.shareWeibo();

    }

    /**
     * 分享qq
     */
    public void shareQQ() {
        mSharePolicy.shareQQ();
    }

    /**
     * 分享qq空间
     */
    public void shareZone() {
        mSharePolicy.shareZone();
    }

    private void requestSearchResult(Bundle bundle) {
        SearchResult[] datas = ((ApiList) bundle.getSerializable("info")).data;
        mRootView.setFans(bundle.getInt("view_count") + "");
        initAdapter();//初始化
        for (SearchResult data : datas) {//添加数据
            mSearchResults.add(data);
        }
        if (datas.length == 2) {//如果只有两个填充到三个,为了展示效果好看
            mSearchResults.add(datas[datas.length - 1]);
        }
        mRootView.setAdapter(mAdapter);//设置适配器
    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mSearchResults == null) {//如果list为空说明第一次拉取列表
            mSearchResults = new ArrayList<>();
            mAdapter = new RecommendListAdapter(mSearchResults);
            initItemListener();
        }
    }

    private void initItemListener() {
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(View view, SearchResult data) {
                startPlay(data);
            }
        });
    }


    /**
     * 关注用户
     */
    public void follow() {
        mModel.followUser(UserService.STATUS_FOLLOW + "", mUserId
                , ZhiboApplication.userInfo.auth_accesskey
                , ZhiboApplication.userInfo.auth_secretkey)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribe(new Action1<BaseJson<FollowInfo>>() {
            @Override
            public void call(BaseJson<FollowInfo> json) {
                if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                    mRootView.showMessage(UiUtils.getString("str_follow_success"));
                    mRootView.setFollowStatus(true);//更改关注按钮状态
                }
                else {
                    mRootView.showMessage(json.message);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage(UiUtils.getString("str_net_erro"));
            }
        });
    }

    /**
     * 查询关注状态
     */
    public void queryFollow() {
        if (mUserId == null || ZhiboApplication.userInfo == null) {
            return;

        }
        mQuerySubscribe = mModel.followUser(UserService.STATUS_FOLLOW_QUERY + "", mUserId
                , ZhiboApplication.userInfo.auth_accesskey
                , ZhiboApplication.userInfo.auth_secretkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<FollowInfo>>() {
                    @Override
                    public void call(BaseJson<FollowInfo> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.setFollowStatus(isFollow(json.data.is_follow));//更改关注按钮状态
                        }
                        else {
                            mRootView.showMessage(json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    /**
     * 是否关注
     *
     * @param i
     * @return
     */
    public static boolean isFollow(int i) {
        if (i == 1) {
            return true;
        }
        return false;
    }


    private void startPlay(SearchResult data) {
        Intent intent = new Intent(UiUtils.getContext(), LivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putBoolean("isVideo", false);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    /**
     * 初始化观众结束页面的布局
     */
    private void initAudienceLayout() {
        mRootView.setGoldVisible(false);//隐藏金币
        mRootView.setStarVisible(false);//隐藏赞
        mRootView.setRecycleViewVisible(true);//显示recycleview
        mRootView.initListener();
        queryFollow();//查询关注状态
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mQuerySubscribe);//解除订阅
    }
}
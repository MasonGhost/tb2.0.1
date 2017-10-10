package com.zhiyicx.zhibolibrary.presenter;

import android.content.Context;

import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.app.policy.SharePolicy;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.UserHomeModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.ShareContent;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.view.UserHomeView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/23.
 */
@ActivityScope
public class UserHomePresenter extends BasePresenter<UserHomeModel, UserHomeView> {
    private SearchResult mUserInfo;
    private Subscription mfollowSubscription;
    private Subscription mQuerySubscribe;
    private SharePolicy mSharePolicy;

    @Inject
    public UserHomePresenter(UserHomeModel model, UserHomeView rootView, @Named("userHome") SharePolicy policy) {
        super(model, rootView);
        this.mSharePolicy = policy;
    }

    public void follow(String action) {
        mUserInfo = mRootView.getUserInfo();
        //设置关注按钮状态
        mfollowSubscription = mModel.followUser(action, mUserInfo.user.uid
                , ZhiboApplication.userInfo.auth_accesskey
                , ZhiboApplication.userInfo.auth_secretkey)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        mRootView.setFollowEnable(false);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setFollowEnable(true);
                    }
                }).subscribe(new Action1<BaseJson<FollowInfo>>() {
                    @Override
                    public void call(BaseJson<FollowInfo> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.setFollow(isFollow(json.data.is_follow));//设置关注按钮状态
                        }
                        else {
                            mRootView.showMessage(json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.setFollowEnable(true);
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));
                    }
                });
    }

    /**
     * 查询关注状态
     */
    public void queryFollow() {
        mUserInfo = mRootView.getUserInfo();
        mQuerySubscribe = mModel.followUser(UserService.STATUS_FOLLOW_QUERY + "", mUserInfo.user.uid
                , ZhiboApplication.userInfo.auth_accesskey
                , ZhiboApplication.userInfo.auth_secretkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<FollowInfo>>() {
                    @Override
                    public void call(BaseJson<FollowInfo> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.setFollow(isFollow(json.data.is_follow));
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

    /**
     * 是否关注
     *
     * @return
     */

    public static String isFollow(boolean isFollow) {
        if (isFollow) {//关注
            return "1";
        }
        return "2";    //取消关注
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mfollowSubscription);
        unSubscribe(mQuerySubscribe);
    }

    /**
     * 分享
     */
    public void showshare(UserInfo presenterUser, Context context) {
        mSharePolicy.setShareContent(ShareContent.getShareContentByUserInfo(presenterUser));
        mSharePolicy.showShare();
    }
}

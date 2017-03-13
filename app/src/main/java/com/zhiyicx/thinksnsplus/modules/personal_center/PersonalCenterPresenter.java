package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class PersonalCenterPresenter extends BasePresenter<PersonalCenterContract.Repository, PersonalCenterContract.View> implements PersonalCenterContract.Presenter {
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    IUploadRepository mIUploadRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public PersonalCenterPresenter(PersonalCenterContract.Repository repository, PersonalCenterContract.View rootView) {
        super(repository, rootView);
    }

    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore, long user_id) {
        Subscription subscription = mRepository.getDynamicListForSomeone(user_id, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<DynamicBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore, long user_id) {
        return mDynamicBeanGreenDao.getMyDynamics(user_id);
    }

    @Override
    public void initFollowState(long user_id) {
        Subscription subscription = mRepository.getUserFollowState(user_id + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<FollowFansBean>() {
                    @Override
                    protected void onSuccess(FollowFansBean data) {
                        mRootView.setFollowState(data);
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleFollow(FollowFansBean followFansBean) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        if (followFansBean.getOrigin_follow_status() == FollowFansBean.UNFOLLOWED_STATE) {
            // 当前未关注，进行关注
            followFansBean.setOrigin_follow_status(FollowFansBean.IFOLLOWED_STATE);
            EventBus.getDefault().post(FollowFansBean.IFOLLOWED_STATE, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER);
        } else {
            // 已关注，取消关注
            followFansBean.setOrigin_follow_status(FollowFansBean.UNFOLLOWED_STATE);
            EventBus.getDefault().post(FollowFansBean.UNFOLLOWED_STATE, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getTargetUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mRootView.setFollowState(followFansBean);
    }

    @Override
    public void uploadUserCover(String filePath) {
        Subscription subscription = mIUploadRepository.upLoadSingleFile("pic",
                filePath, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.setUpLoadCoverState(true, data);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.setUpLoadCoverState(false, 0);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadCoverState(false, 0);
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changeUserCover(final UserInfoBean userInfoBean, int storage_task_id, final String imagePath) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cover_storage_task_id", storage_task_id + "");
        Subscription subscription = mUserInfoRepository.changeUserInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面
                        mRootView.setChangeUserCoverState(true);
                        // 将本地图片路径作为storageId保存到数据库
                        userInfoBean.setAvatar(imagePath);
                        // 更新用户数据库
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    }

                    @Override
                    protected void onFailure(String message) {
                        // 修改失败，好尴尬
                        mRootView.setChangeUserCoverState(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setChangeUserCoverState(false);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        mDynamicBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void setCurrentUserInfo(Long userId) {
        Subscription subscription = mRepository.getCurrentUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mRootView.setHeaderInfo(data);
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }


}

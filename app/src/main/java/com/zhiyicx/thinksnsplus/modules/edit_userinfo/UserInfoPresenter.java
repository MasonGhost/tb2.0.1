package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserTagBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class UserInfoPresenter extends BasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {

    @Inject
    UpLoadRepository mIUploadRepository;
    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    UserTagBeanGreenDaoImpl mUserTagBeanGreenDao;

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    public UserInfoPresenter(UserInfoContract.View rootView) {
        super(rootView);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Override
    public void changeUserHeadIcon(String filePath) {
        mRootView.setUpLoadHeadIconState(0, "");
        Subscription subscription = mIUploadRepository.uploadAvatar(filePath)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        UserInfoBean currentLoginUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getmCurrentLoginAuth()
                                .getUser_id());
                        currentLoginUserInfo.setAvatar(filePath);
                        mUserInfoBeanGreenDao.insertOrReplace(currentLoginUserInfo);
                        ImageUtils.updateCurrentLoginUserHeadPicSignature(mContext);
                        mRootView.setUpLoadHeadIconState(1, "");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.setUpLoadHeadIconState(-1, message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadHeadIconState(-1, "");
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changUserInfo(final HashMap<String, Object> userInfos, final boolean isHeadIcon) {
        if (!checkChangedUserInfo(userInfos)) {
            return;
        }
        // 上传头像就不需要提示
        if (!isHeadIcon) {
            mRootView.setChangeUserInfoState(0, "");
        }
        Subscription subscription = mUserInfoRepository.changeUserInfo(userInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Object>() {

                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面
                        if (!isHeadIcon) {
                            mRootView.setChangeUserInfoState(1, "");
                        } else {
                            mRootView.setUpLoadHeadIconState(2, "");
                        }
                        EventBus.getDefault().post(EventBusTagConfig.EVENT_USERINFO_UPDATE);
                        upDateUserInfo(userInfos);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 修改失败，好尴尬
                        if (!isHeadIcon) {
                            mRootView.setChangeUserInfoState(-1, message);
                        } else {
                            mRootView.setUpLoadHeadIconState(-1, message);
                        }

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        if (!isHeadIcon) {
                            mRootView.setChangeUserInfoState(-1, "");
                        } else {
                            mRootView.setUpLoadHeadIconState(-1, "");
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getCurrentUserTags() {
        List<UserTagBean> datas = mUserTagBeanGreenDao.getCurrentUsertags();
        if (datas.isEmpty()) {
            getCurrentUserTagsFromNet();
        } else {
            mRootView.updateTags(datas);
        }

    }

    private void getCurrentUserTagsFromNet() {
        Subscription sub = mUserInfoRepository.getCurrentUserTags()
                .map(userTagBeens -> {
                    for (UserTagBean userTagBean : userTagBeens) {
                        userTagBean.setMine_has(true);
                    }
                    mUserTagBeanGreenDao.saveMultiData(userTagBeens);
                    return userTagBeens;
                })
                .subscribe(new BaseSubscribeForV2<List<UserTagBean>>() {
                    @Override
                    protected void onSuccess(List<UserTagBean> data) {
                        mRootView.updateTags(data);
                    }
                });
        addSubscrebe(sub);
    }

    @Override
    public void initUserInfo() {
        AuthBean authBean = mIAuthRepository.getAuthBean();
        UserInfoBean mUserInfoBean = null;
        if (authBean != null) {
            mUserInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(authBean.getUser_id());
        }
        if (mUserInfoBean == null) {
            mUserInfoBean = new UserInfoBean();
        }
        mRootView.initUserInfo(mUserInfoBean);
    }

    /**
     * 更新数据库信息
     *
     * @param changeUserInfo 被编辑过的的用户信息
     */
    private void upDateUserInfo(HashMap<String, Object> changeUserInfo) {
        AuthBean authBean = mIAuthRepository.getAuthBean();
        UserInfoBean mUserInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(authBean
                .getUser_id());
        if (changeUserInfo.containsKey(UserInfoFragment.USER_NAME)) {
            mUserInfoBean.setName((String) changeUserInfo.get(UserInfoFragment.USER_NAME));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_SEX)) {
            mUserInfoBean.setSex((Integer) changeUserInfo.get(UserInfoFragment.USER_SEX));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_LOCATION)) {
            mUserInfoBean.setLocation((String) changeUserInfo.get(UserInfoFragment.USER_LOCATION));
//            mUserInfoBean.setProvince((String) changeUserInfo.get(UserInfoFragment.USER_PROVINCE));
//            mUserInfoBean.setCity((String) changeUserInfo.get(UserInfoFragment.USER_CITY));
//            if (changeUserInfo.containsKey(UserInfoFragment.USER_AREA)) {
//                mUserInfoBean.setArea((String) changeUserInfo.get(UserInfoFragment.USER_AREA));
//            }
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_INTRO)) {
            mUserInfoBean.setIntro((String) changeUserInfo.get(UserInfoFragment.USER_INTRO));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_STORAGE_TASK_ID)) {
            mUserInfoBean.setAvatar((String) changeUserInfo.get(UserInfoFragment.USER_LOCAL_IMG_PATH));
        }
        // 提示用户主页更新用户信息
        List<UserInfoBean> userInfoBeanList = new ArrayList<>();
        userInfoBeanList.add(mUserInfoBean);
        EventBus.getDefault().post(userInfoBeanList, EventBusTagConfig.EVENT_USERINFO_UPDATE);
        // 修改数据库内容
        mUserInfoBeanGreenDao.insertOrReplace(mUserInfoBean);
    }

    /**
     * 修改用户信息前，校验信息
     *
     * @return 返回true，表示可以通知服务器
     */
    private boolean checkChangedUserInfo(HashMap<String, Object> userInfos) {
        if (userInfos != null) {
            // 如果修改信息包含用户名，并且用户名无法通过检测，返回false
            if (userInfos.containsKey(UserInfoFragment.USER_NAME)) {
                if (!checkUsername((String) userInfos.get(UserInfoFragment.USER_NAME))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_length), mContext.getResources()
                .getInteger(R.integer.username_max_length))) {
            mRootView.setChangeUserInfoState(-1, mContext.getString(R.string.username_toast_hint));
            return false;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.setChangeUserInfoState(-1, mContext.getString(R.string.username_toast_not_number_start_hint));
            return false;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.setChangeUserInfoState(-1, mContext.getString(R.string.username_toast_not_symbol_hint));
            return false;
        }
        return true;
    }


}

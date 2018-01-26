package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import android.graphics.BitmapFactory;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
//import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class HeadPortraitViewPresenter extends AppBasePresenter< HeadPortraitViewContract.View>
        implements HeadPortraitViewContract.Presenter {


//    @Inject
//    IUploadRepository mIUploadRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public HeadPortraitViewPresenter( HeadPortraitViewContract.View rootView) {
        super( rootView);
    }

    @Override
    public UserInfoBean getCurrentUser(long user_id) {
        return mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
    }

    @Override
    public void changeUserHeadIcon(String filePath) {
//        mRootView.setUpLoadHeadIconState(0, 0);
//        BitmapFactory.Options options = DrawableProvider.getPicsWHByFile(filePath);
//        Subscription subscription = mIUploadRepository.upLoadSingleFileV2(
//                filePath, options.outMimeType, true, options.outWidth, options.outHeight)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscribe<Integer>() {
//                    @Override
//                    protected void onSuccess(Integer data) {
//                        mRootView.setUpLoadHeadIconState(1, data);
//                    }
//
//                    @Override
//                    protected void onFailure(String message, int code) {
//                        mRootView.setUpLoadHeadIconState(-1, 0);
//                    }
//
//                    @Override
//                    protected void onException(Throwable throwable) {
//                        mRootView.setUpLoadHeadIconState(-1, 0);
//                        LogUtils.e(throwable, "result");
//                    }
//                });
//        addSubscrebe(subscription);
    }

    @Override
    public void updateUserInfo(HashMap<String, String> userInfo, int id) {
//        Subscription subscription = mUserInfoRepository.changeUserInfo(userInfo)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscribeForV2<BaseJson>() {
//
//                    @Override
//                    protected void onSuccess(BaseJson data) {
//                        // 修改成功后，关闭页面
//                        mRootView.setUpLoadHeadIconState(2, 0);
//                        EventBus.getDefault().post(EventBusTagConfig.EVENT_USERINFO_UPDATE);
//                        upDateUserInfo(id);
//                    }
//
//                    @Override
//                    protected void onFailure(String message, int code) {
//                        // 修改失败，好尴尬
//                        mRootView.setUpLoadHeadIconState(-1, 0);
//                    }
//
//                    @Override
//                    protected void onException(Throwable throwable) {
//                        mRootView.setUpLoadHeadIconState(-1, 0);
//                    }
//                });
//        addSubscrebe(subscription);

    }

    private void upDateUserInfo(int id) {
        long user_id = AppApplication.getmCurrentLoginAuth().getUser_id();
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
        if (userInfoBean != null && id != 0){
            userInfoBean.setAvatar(String.valueOf(id));
            // 提示用户主页更新用户信息
            List<UserInfoBean> userInfoBeanList = new ArrayList<>();
            userInfoBeanList.add(userInfoBean);
            EventBus.getDefault().post(userInfoBeanList, EventBusTagConfig.EVENT_USERINFO_UPDATE);
            mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
        } else {
            mRootView.setUpLoadHeadIconState(-1, 0);
        }
    }
}

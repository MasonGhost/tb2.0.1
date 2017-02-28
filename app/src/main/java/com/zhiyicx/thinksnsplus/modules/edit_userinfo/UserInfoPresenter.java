package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.graphics.BitmapFactory;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class UserInfoPresenter extends BasePresenter<UserInfoContract.Repository,
        UserInfoContract.View> implements UserInfoContract.Presenter {

    @Inject
    IUploadRepository mIUploadRepository;
    @Inject
    IAuthRepository mIAuthRepository;
    public UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public UserInfoPresenter(UserInfoContract.Repository repository, UserInfoContract.View
            rootView) {
        super(repository, rootView);
        mUserInfoBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .userInfoBeanGreenDao();
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getAreaData() {
        Subscription subscription = mRepository.getAreaList()
                .subscribe(new Action1<ArrayList<AreaBean>>() {
                    @Override
                    public void call(ArrayList<AreaBean> areaBean) {
                        ArrayList<ArrayList<AreaBean>> areaBeenChildList = new
                                ArrayList<ArrayList<AreaBean>>();
                        ArrayList<ArrayList<ArrayList<AreaBean>>> areaBeenChildList1 = new
                                ArrayList<ArrayList<ArrayList<AreaBean>>>();
                        // 处理第二级联动
                        for (AreaBean areaBean1 : areaBean) {
                            ArrayList<AreaBean> areaBean1List = areaBean1.getChild();
                            areaBeenChildList.add(areaBean1List);
                            // 处理第三级连动
                            if (areaBean1List != null) {
                                ArrayList<ArrayList<AreaBean>> arrayListArrayList = new
                                        ArrayList<ArrayList<AreaBean>>();
                                for (AreaBean areaBean2 : areaBean1List) {
                                    ArrayList<AreaBean> areaBean2List = areaBean2.getChild();
                                    arrayListArrayList.add(areaBean2List);
                                }
                                areaBeenChildList1.add(arrayListArrayList);
                            }
                        }
                        mRootView.setAreaData(areaBean, areaBeenChildList, areaBeenChildList1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        com.zhiyicx.common.utils.log.LogUtils.e(throwable, mContext.getString(R
                                .string.data_load_error));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changeUserHeadIcon(String filePath) {
        Subscription subscription = mIUploadRepository.upLoadSingleFile("pic",
                filePath,true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.setUpLoadHeadIconState(true, data);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.setUpLoadHeadIconState(false, 0);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadHeadIconState(false, 0);
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changUserInfo(final HashMap<String, String> userInfos) {
        Subscription subscription = mRepository.changeUserInfo(userInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面
                        mRootView.setChangeUserInfoState();
                        upDateUserInfo(userInfos);
                    }

                    @Override
                    protected void onFailure(String message) {
                        // 修改失败，好尴尬

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void initUserInfo() {
        AuthBean authBean = mIAuthRepository.getAuthBean();
        UserInfoBean mUserInfoBean = null;
        if (authBean != null) {
            mUserInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean.getUser_id());
        }
        if (mUserInfoBean == null) {
            mUserInfoBean = new UserInfoBean();
        }
        mRootView.initUserInfo(mUserInfoBean);
    }

    /**
     * 更新数据库信息
     *
     * @param changeUserInfo
     */
    private void upDateUserInfo(HashMap<String, String> changeUserInfo) {
        AuthBean authBean = mIAuthRepository.getAuthBean();
        UserInfoBean mUserInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean
                .getUser_id());
        if (changeUserInfo.containsKey("name")) {
            mUserInfoBean.setName(changeUserInfo.get("name"));
        }
        if (changeUserInfo.containsKey("sex")) {
            mUserInfoBean.setSex(changeUserInfo.get("sex"));
        }
        if (changeUserInfo.containsKey("location")) {
            mUserInfoBean.setLocation(changeUserInfo.get("location"));
        }
        if (changeUserInfo.containsKey("intro")) {
            mUserInfoBean.setIntro(changeUserInfo.get("intro"));
        }
        if (changeUserInfo.containsKey("avatar")) {
            mUserInfoBean.setUserIcon(changeUserInfo.get("avatar"));
        }

        mUserInfoBeanGreenDao.insertOrReplace(mUserInfoBean);
    }


}

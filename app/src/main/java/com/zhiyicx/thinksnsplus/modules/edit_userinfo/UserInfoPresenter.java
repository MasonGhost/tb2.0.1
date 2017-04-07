package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.graphics.BitmapFactory;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;

import org.simple.eventbus.EventBus;

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

    @Override
    protected boolean useEventBus() {
        return true;
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
        mRootView.setUpLoadHeadIconState(0, 0);
        BitmapFactory.Options options = DrawableProvider.getPicsWHByFile(filePath);
        Subscription subscription = mIUploadRepository.upLoadSingleFile("pic",
                filePath, options.outMimeType, true, options.outWidth, options.outHeight)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.setUpLoadHeadIconState(1, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.setUpLoadHeadIconState(-1, 0);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadHeadIconState(-1, 0);
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changUserInfo(final HashMap<String, String> userInfos) {
        if (!checkChangedUserInfo(userInfos)) {
            return;
        }
        mRootView.setChangeUserInfoState(0, "");
        Subscription subscription = mRepository.changeUserInfo(userInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面
                        mRootView.setChangeUserInfoState(1, "");
                        EventBus.getDefault().post(EventBusTagConfig.EVENT_USERINFO_UPDATE);
                        upDateUserInfo(userInfos);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 修改失败，好尴尬
                        mRootView.setChangeUserInfoState(-1, "");
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setChangeUserInfoState(-1, "");
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
     * @param changeUserInfo 被编辑过的的用户信息
     */
    private void upDateUserInfo(HashMap<String, String> changeUserInfo) {
        AuthBean authBean = mIAuthRepository.getAuthBean();
        UserInfoBean mUserInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean
                .getUser_id());
        if (changeUserInfo.containsKey(UserInfoFragment.USER_NAME)) {
            mUserInfoBean.setName(changeUserInfo.get(UserInfoFragment.USER_NAME));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_SEX)) {
            mUserInfoBean.setSex(changeUserInfo.get(UserInfoFragment.USER_SEX));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_LOCATION)) {
            mUserInfoBean.setLocation(changeUserInfo.get(UserInfoFragment.USER_LOCATION));
            mUserInfoBean.setProvince(changeUserInfo.get(UserInfoFragment.USER_PROVINCE));
            mUserInfoBean.setCity(changeUserInfo.get(UserInfoFragment.USER_CITY));
            if (changeUserInfo.containsKey(UserInfoFragment.USER_AREA)) {
                mUserInfoBean.setArea(changeUserInfo.get(UserInfoFragment.USER_AREA));
            }
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_INTRO)) {
            mUserInfoBean.setIntro(changeUserInfo.get(UserInfoFragment.USER_INTRO));
        }
        if (changeUserInfo.containsKey(UserInfoFragment.USER_STORAGE_TASK_ID)) {
            mUserInfoBean.setAvatar(changeUserInfo.get(UserInfoFragment.USER_LOCAL_IMG_PATH));
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
    private boolean checkChangedUserInfo(HashMap<String, String> userInfos) {
        if (userInfos != null) {
            // 如果修改信息包含用户名，并且用户名无法通过检测，返回false
            if (userInfos.containsKey(UserInfoFragment.USER_NAME)) {
                if (!checkUsername(userInfos.get(UserInfoFragment.USER_NAME))) {
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
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_length), mContext.getResources().getInteger(R.integer.username_max_length))) {
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

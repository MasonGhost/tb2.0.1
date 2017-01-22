package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class UserInfoPresenter extends BasePresenter<UserInfoContract.Repository, UserInfoContract.View> implements UserInfoContract.Presenter {

    @Inject
    private IUploadRepository mIUploadRepository;

    @Inject
    public UserInfoPresenter(UserInfoContract.Repository repository, UserInfoContract.View rootView) {
        super(repository, rootView);
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
                    public void call(ArrayList<AreaBean> areaBeen) {
                        ArrayList<ArrayList<AreaBean>> areaBeenChildList = new ArrayList<ArrayList<AreaBean>>();
                        for (AreaBean areaBean : areaBeen) {
                            areaBeenChildList.add(areaBean.getChild());
                        }
                        mRootView.setAreaData(areaBeen, areaBeenChildList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        com.zhiyicx.common.utils.log.LogUtils.e(throwable, mContext.getString(R.string.data_load_error));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changeUserHeadIcon(String hash, String fileName, Map<String, String> filePathList) {
        Subscription subscription = mIUploadRepository.upLoadSingleFile(hash, fileName, filePathList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson>() {
                    @Override
                    public void call(BaseJson baseJson) {
                        if (baseJson.isStatus()) {
                            ToastUtils.showToast("头像上传成功");
                        } else {
                            ToastUtils.showToast("头像上传失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        com.zhiyicx.common.utils.log.LogUtils.e(throwable, "headIcon");
                        //ToastUtils.showToast("-->" + throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changUserInfo(HashMap<String, String> userInfos) {
        Subscription subscription = mRepository.changeUserInfo(userInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面

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

}

package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.imsdk.utils.common.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public void onDestroy() {

    }

    @Override
    public void getAreaData() {
        mRepository.getAreaList()
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
    }

    @Override
    public void changeUserHeadIcon(String hash, String fileName, Map<String, String> filePathList) {
        mRepository.changeUserHeadIcon(hash, fileName, filePathList)
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
                        com.zhiyicx.common.utils.log.LogUtils.e(throwable,"headIcon");
                        //ToastUtils.showToast("-->" + throwable.getMessage());
                    }
                });
    }

}

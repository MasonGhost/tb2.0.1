package com.zhiyicx.thinksnsplus.modules.settings;

import android.text.TextUtils;

import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommonRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements SettingsContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    CommonRepository mCommonRepository;

    @Inject
    public SettingsPresenter(SettingsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getDirCacheSize() {
        Subscription getCacheDirSizeSub = mCommonRepository.getDirCacheSize()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(size -> {
                    if (TextUtils.isEmpty(size)) {
                        mRootView.setCacheDirSize(mContext.getString(R.string.cache_zero_size));//将缓存大小改为 0b
                    } else {
                        mRootView.setCacheDirSize(size);
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(getCacheDirSizeSub);
    }

    @Override
    public void cleanCache() {
        Subscription cleanCacheSub = mCommonRepository.cleanCache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isDelete -> {
                    if (isDelete) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.clean_success));// 删除成功
                        mRootView.setCacheDirSize(mContext.getString(R.string.cache_zero_size));//将缓存大小改为 0b
                    } else {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.clean_failure));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.clean_failure));
                });
        addSubscrebe(cleanCacheSub);
    }

    @Override
    public boolean loginOut() {
        mIAuthRepository.clearAuthBean();
        mIAuthRepository.clearThridAuth();
        return true;
    }

    @Override
    public void checkUpdate() {
        Subscription subscribe = mSystemRepository.getAppNewVersion()
                .subscribe(new BaseSubscribeForV2<List<AppVersionBean>>() {
                    @Override
                    protected void onSuccess(List<AppVersionBean> data) {
                        mRootView.getAppNewVersionSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);

    }
    @Override
    public List<SystemConfigBean.ImHelperBean> getImHelper() {
        return mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
    }

}

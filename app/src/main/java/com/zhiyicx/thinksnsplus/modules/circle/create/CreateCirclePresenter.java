package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleTypeBeanGreenDaoImpl;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCirclePresenter extends AppBasePresenter<CreateCircleContract.Repository, CreateCircleContract.View>
        implements CreateCircleContract.Presenter {

    @Inject
    CircleTypeBeanGreenDaoImpl mCircleTypeBeanGreenDao;

    @Inject
    public CreateCirclePresenter(CreateCircleContract.Repository repository, CreateCircleContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public String getCircleCategoryName(int category) {
        return mCircleTypeBeanGreenDao.getCategoryNameById(category);
    }

    @Override
    public void createCircle(CreateCircleBean createCircleBean) {
        Subscription subscription = mRepository.createCircle(createCircleBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CircleInfo>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CircleInfo> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.create_reviewing));
                        mRootView.setCircleInfo(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateCircle(CreateCircleBean createCircleBean) {
        Subscription subscription = mRepository.updateCircle(createCircleBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CircleInfo>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CircleInfo> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.create_reviewing));
                        mRootView.setCircleInfo(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }
}

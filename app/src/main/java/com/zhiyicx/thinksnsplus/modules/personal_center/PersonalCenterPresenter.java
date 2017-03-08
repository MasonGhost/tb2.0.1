package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

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
    public PersonalCenterPresenter(PersonalCenterContract.Repository repository, PersonalCenterContract.View rootView) {
        super(repository, rootView);
    }

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
                        // 添加动态条数的数据,用来显示动态数量的item
                        if (data != null) {
                            int dynamicCount = data.size();
                            DynamicBean dynamicBean = new DynamicBean();
                        }
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
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return false;
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

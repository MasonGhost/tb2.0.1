package com.zhiyicx.thinksnsplus.modules.tb.contract;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by lx on 2018/3/23.
 */

public class ContractListPresenter extends AppBasePresenter<ContractListContract.View> implements ContractListContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ContractListPresenter(ContractListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getContract()
                .map(userInfoBeans -> {
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeans);
                    List<HintSideBarUserBean> datas = new ArrayList<>();
                    for (UserInfoBean userInfoBean : userInfoBeans) {
                        HintSideBarUserBean user = new HintSideBarUserBean(userInfoBean.getUser_id() + "", userInfoBean.getAvatar(),
                                userInfoBean.getName());
                        datas.add(user);
                    }
                    return datas;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<HintSideBarUserBean>>() {
                    @Override
                    protected void onSuccess(List<HintSideBarUserBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, isLoadMore);

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public UserInfoBean getLocalUsrinfo(String id) {
        try {
            return mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<HintSideBarUserBean> data, boolean isLoadMore) {
        return false;
    }
}

package com.zhiyicx.thinksnsplus.modules.information.dig;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListContract.Presenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class InfoDigListPresenter extends AppBasePresenter<InfoDigListContract.View> implements Presenter{

    @Inject
    BaseInfoRepository mInfoDetailsRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public InfoDigListPresenter(InfoDigListContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mInfoDetailsRepository.getInfoDigListV2(String.valueOf(mRootView.getNesId()), maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<InfoDigListBean>>() {

                    @Override
                    protected void onSuccess(List<InfoDigListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
      mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoDigListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleFollowUser(int position, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDataFollowState(position);
    }
}

package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CirclePostDetailRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoDetailsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.BaseDigItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 17/12/11 16:03
 * @Email Jliuer@aliyun.com
 * @Description 
 */
@FragmentScoped
public class DigListPresenter extends AppBasePresenter<DigListContract.Repository, DigListContract.View> implements DigListContract.Presenter {

    @Inject
    InfoDetailsRepository mInfoDetailsRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    CirclePostDetailRepository mCirclePostDetailRepository;
    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    public DigListPresenter(DigListContract.Repository repository, DigListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Observable observable = null;
        switch (mRootView.getType()) {
            case BaseDigItem.TYPE_INFO:
                observable = mInfoDetailsRepository.getInfoDigListV2(String.valueOf(mRootView.getSourceId()), maxId);
                break;
            case BaseDigItem.TYPE_POST:
                observable = mCirclePostDetailRepository.getPostDigList(mRootView.getSourceId(), TSListFragment.DEFAULT_ONE_PAGE_SIZE, maxId);
                break;
            case BaseDigItem.TYPE_DYNAMIC:
                observable = mBaseDynamicRepository.getDynamicDigListV2(mRootView.getSourceId(), maxId);
                break;
            default:
        }

        Subscription subscription = observable.subscribe(new BaseSubscribeForV2() {
            @Override
            protected void onSuccess(Object data) {
                List<BaseListBean> result = (List<BaseListBean>) data;
                mRootView.onNetResponseSuccess(result, isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
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
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleFollowUser(int position, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDataFollowState(position);
    }
}

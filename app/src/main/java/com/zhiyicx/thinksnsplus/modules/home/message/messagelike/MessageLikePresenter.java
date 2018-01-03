package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessageLikePresenter extends AppBasePresenter<MessageLikeContract.View> implements MessageLikeContract
        .Presenter {
    @Inject
    DigedBeanGreenDaoImpl mDigedBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MessageLikePresenter(MessageLikeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription commentSub = mUserInfoRepository.getMyDiggs(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<DigedBean>>() {
                    @Override
                    protected void onSuccess(List<DigedBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
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
        addSubscrebe(commentSub);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (isLoadMore) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(), true);

        } else {
            mRootView.onCacheResponseSuccess(mDigedBeanGreenDao.getMultiDataFromCache(), false);

        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DigedBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            mDigedBeanGreenDao.clearTable();
        }
        mDigedBeanGreenDao.saveMultiData(data);
        return true;
    }
}

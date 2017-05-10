package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;

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
public class MessageLikePresenter extends BasePresenter<MessageLikeContract.Repository, MessageLikeContract.View> implements MessageLikeContract.Presenter {
    @Inject
    DigedBeanGreenDaoImpl mDigedBeanGreenDao;

    @Inject
    public MessageLikePresenter(MessageLikeContract.Repository repository, MessageLikeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription commentSub = mRepository.getMyDiggs(maxId.intValue())
                .subscribe(new BaseSubscribe<List<DigedBean>>() {
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
    public List<DigedBean> requestCacheData(Long maxId, boolean isLoadMore) {
        if (isLoadMore) {
            return new ArrayList<>();
        }
        return mDigedBeanGreenDao.getMultiDataFromCache();
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

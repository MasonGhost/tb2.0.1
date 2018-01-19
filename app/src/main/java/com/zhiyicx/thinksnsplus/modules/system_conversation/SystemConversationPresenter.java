package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationPresenter extends AppBasePresenter< SystemConversationContract.View> implements SystemConversationContract.Presenter {
    @Inject
    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    @Inject
    public SystemConversationPresenter( SystemConversationContract.View rootView) {
        super(rootView);
    }


    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription systemconversationsSub = mSystemRepository.getSystemConversations(maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribe(new BaseSubscribeForV2<List<SystemConversationBean>>() {
                    @Override
                    protected void onSuccess(List<SystemConversationBean> data) {
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
        addSubscrebe(systemconversationsSub);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mSystemConversationBeanGreenDao.getMultiDataFromCache(),isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List data, boolean isLoadMore) {
        // 刷新的时候清除数据
        if (!isLoadMore) {
            mSystemConversationBeanGreenDao.clearTable();
        }
        mSystemConversationBeanGreenDao.saveMultiData(data);
        return true;
    }
}

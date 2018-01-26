package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.PostDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.QAPublishBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/22/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DraftBoxPresenter extends AppBasePresenter<DraftBoxContract.View> implements DraftBoxContract.Presenter {

    @Inject
    QAPublishBeanGreenDaoImpl mQAPublishBeanGreenDaoImpl;

    @Inject
    AnswerDraftBeanGreenDaoImpl mAnswerDraftBeanGreenDaoImpl;

    @Inject
    PostDraftBeanGreenDaoImpl mPostDraftBeanGreenDao;

    @Override
    protected boolean useEventBus() {
        return super.useEventBus();
    }

    @Inject
    public DraftBoxPresenter(DraftBoxContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRootView.onNetResponseSuccess(requestCacheData(), false);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    public List<BaseDraftBean> requestCacheData() {
        if (DraftBoxFragment.MY_DRAFT_TYPE_QUESTION.equals(mRootView.getDraftType())) {
            return mQAPublishBeanGreenDaoImpl.getMultiBasetDraftDataFromCache();
        } else if (DraftBoxFragment.MY_DRAFT_TYPE_ANSWER.equals(mRootView.getDraftType())) {
            return mAnswerDraftBeanGreenDaoImpl.getMultiBasetDraftDataFromCache();
        } else if (DraftBoxFragment.MY_DRAFT_TYPE_POST.equals(mRootView.getDraftType())) {
            return mPostDraftBeanGreenDao.getMultiBasetDraftDataFromCache();
        }
        return null;
    }

    @Override
    public void deleteDraft(BaseDraftBean draftBean) {
        if (draftBean instanceof QAPublishBean) {
            QAPublishBean deleteData = (QAPublishBean) draftBean;
            mQAPublishBeanGreenDaoImpl.deleteSingleCache(deleteData);
        } else if (draftBean instanceof AnswerDraftBean) {
            AnswerDraftBean deleteData = (AnswerDraftBean) draftBean;
            mAnswerDraftBeanGreenDaoImpl.deleteSingleCache(deleteData);
        } else if (draftBean instanceof PostDraftBean) {
            PostDraftBean deleteData = (PostDraftBean) draftBean;
            mPostDraftBeanGreenDao.deleteSingleCache(deleteData);
        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseDraftBean> data, boolean isLoadMore) {
        return false;
    }
}

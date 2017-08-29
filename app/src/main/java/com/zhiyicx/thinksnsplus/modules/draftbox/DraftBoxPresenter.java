package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerDraftBeanGreenDaoImpl;
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
public class DraftBoxPresenter extends AppBasePresenter<DraftBoxContract.Repository, DraftBoxContract.View> implements DraftBoxContract.Presenter {

    @Inject
    QAPublishBeanGreenDaoImpl mQAPublishBeanGreenDaoImpl;

    @Inject
    AnswerDraftBeanGreenDaoImpl mAnswerDraftBeanGreenDaoImpl;

    @Inject
    public DraftBoxPresenter(DraftBoxContract.Repository repository, DraftBoxContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRootView.onNetResponseSuccess(requestCacheData(), false);
    }

    @Override
    public List<BaseDraftBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    public List<BaseDraftBean> requestCacheData() {
        List<BaseDraftBean> answers = mAnswerDraftBeanGreenDaoImpl.getMultiBasetDraftDataFromCache();
        List<BaseDraftBean> questions = mQAPublishBeanGreenDaoImpl.getMultiBasetDraftDataFromCache();
        questions.addAll(answers);
        return questions;
    }

    @Override
    public void deleteDraft(BaseDraftBean draftBean) {
        if (draftBean instanceof QAPublishBean) {
            QAPublishBean deleteData = (QAPublishBean) draftBean;
            mQAPublishBeanGreenDaoImpl.deleteSingleCache(deleteData);
        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseDraftBean> data, boolean isLoadMore) {
        return false;
    }
}

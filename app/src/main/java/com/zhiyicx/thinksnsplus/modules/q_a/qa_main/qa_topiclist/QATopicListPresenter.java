package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicListPresenter extends AppBasePresenter<QATopicListConstact.Repository,QATopicListConstact.View>
        implements QATopicListConstact.Presenter {

    @Inject
    public QATopicListPresenter(QATopicListConstact.Repository repository, QATopicListConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<QATopicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return false;
    }
}

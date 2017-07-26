package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface QATopicListConstact {

    interface View extends ITSListView<QATopicBean,Presenter>{}

    interface Presenter extends ITSListPresenter<QATopicBean>{}

    interface Repository{}
}

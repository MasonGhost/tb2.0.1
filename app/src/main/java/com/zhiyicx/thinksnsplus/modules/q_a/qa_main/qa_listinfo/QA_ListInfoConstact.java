package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface QA_ListInfoConstact {
    interface View extends ITSListView<QAListInfoBean,Presenter>{
        String getQAInfoType();
        void showDeleteSuccess();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean>{
        void requestNetData(String subject, Long maxId,String type,boolean isLoadMore);
        void payForOnlook(long answer_id,int position);
        SystemConfigBean getSystemConfig();
    }

}

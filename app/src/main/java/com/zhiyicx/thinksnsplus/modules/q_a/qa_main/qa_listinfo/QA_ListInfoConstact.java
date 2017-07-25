package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.QA_LIstInfoBean;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface QA_ListInfoConstact {
    interface View extends ITSListView<QA_LIstInfoBean,Presenter>{}

    interface Presenter extends ITSListPresenter<QA_LIstInfoBean>{}

    interface Repository {}
}

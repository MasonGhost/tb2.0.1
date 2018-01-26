package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;

/**
 * @Author Jliuer
 * @Date 2017/08/22/11:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DraftBoxContract {
    interface View extends ITSListView<BaseDraftBean,Presenter> {
        String getDraftType();
    }
    interface Presenter extends ITSListPresenter<BaseDraftBean> {
        void deleteDraft(BaseDraftBean draftBean);
    }
}

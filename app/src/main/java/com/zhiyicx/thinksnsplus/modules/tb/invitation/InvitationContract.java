package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBShareLinkBean;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InvitationContract {
    interface View extends IBaseView<Presenter> {
        void getShareLinkSuccess(TBShareLinkBean data);
    }

    interface Presenter extends IBaseTouristPresenter {
        void getShareLink();

        void shareTask();

    }
}

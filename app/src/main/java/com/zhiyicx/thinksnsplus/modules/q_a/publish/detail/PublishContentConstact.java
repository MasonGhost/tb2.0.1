package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishContentConstact {
    interface View extends IBaseView<Presenter>{
        void uploadPicSuccess(int id);
        void uploadPicFailed();
    }
    interface Presenter extends IBaseTouristPresenter{
        void uploadPic(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight);
    }
    interface Repository{}
}

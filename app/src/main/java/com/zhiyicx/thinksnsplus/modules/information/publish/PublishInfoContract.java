package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishInfoContract {
    interface View extends IBaseView<Presenter> {
        void uploadPicSuccess(int id);

        void uploadPicFailed();

        void publishInfoFailed();

        void publishInfoSuccess();

        boolean showUplaoding();

        void addImageViewAtIndex(String iamge, int iamge_id, String markdonw, boolean isLast);

        void addEditTextAtIndex(String text);

        void onPareseBodyEnd(boolean hasContent);
    }

    interface Presenter extends IBaseTouristPresenter {
        void uploadPic(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight);

        void publishInfo(InfoPublishBean infoPublishBean);

        void pareseBody(String body);
    }

}

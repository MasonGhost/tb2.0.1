package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MarkdownContract {
    interface View<P extends Presenter> extends IBaseView<P> {
        void onUploading(long id, String filePath, int progress, int imageId);
        void onFailed(String filePath, long id);
    }

    interface Presenter extends IBaseTouristPresenter {
        void saveDraft(BaseDraftBean postDraftBean);
        void uploadPic(final String filePath, long tagId);
    }

}

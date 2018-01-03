package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MarkdownContract {
    interface View extends IBaseView<Presenter> {
        void onUploading(long id, String filePath, int progress,int imageId);

        void onFailed(String filePath, long id);

        void sendPostSuccess(CirclePostListBean data);
    }

    interface Presenter extends IBaseTouristPresenter {
        void uploadPic(final String filePath, long tagId);

        void publishPost(PostPublishBean postPublishBean);

        void pareseBody(String body);

        void saveDraft(BaseDraftBean postDraftBean);
    }

}

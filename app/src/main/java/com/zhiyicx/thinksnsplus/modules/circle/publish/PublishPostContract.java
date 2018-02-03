package com.zhiyicx.thinksnsplus.modules.circle.publish;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishPostContract {
    interface View extends MarkdownContract.View<Presenter>{
        void sendPostSuccess(CirclePostListBean data);
    }

    interface Presenter extends MarkdownContract.Presenter{
        void publishPost(PostPublishBean postPublishBean);
    }
}

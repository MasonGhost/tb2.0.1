package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class SendComment implements ICommentEvent<ICommentBean> {

    @Override
    public void handleComment(ICommentBean comment) {
        CommentBean commentBean = comment.get$$Comment();
        sendComment(commentBean);
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", commentBean.getComment_content());
        params.put("reply_to_user_id", commentBean.getReply_to_user_id());
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.POST, params);
        backgroundRequestTaskBean.setPath(commentBean.getNetRequestUrl());
        BackgroundTaskManager.getInstance(BaseApplication.getContext()).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    protected void sendComment(CommentBean commentBean) {

    }
}

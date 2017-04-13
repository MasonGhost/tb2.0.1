package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DeleteComment implements ICommentEvent<ICommentBean> {

    @Override
    public void handleComment(ICommentBean comment) {
        CommentBean commentBean = comment.get$$Comment();
        deleteComment(commentBean);
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_id", commentBean.getComment_id());
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(commentBean.getNetRequestUrl());
        BackgroundTaskManager.getInstance(BaseApplication.getContext()).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    protected void deleteComment(CommentBean commentBean) {

    }
}

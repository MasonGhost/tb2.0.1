package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:44
 * @Email Jliuer@aliyun.com
 * @Description 删除评论处理类
 */
public class DeleteComment implements ICommentEvent<ICommentBean> {

    private BackgroundTaskHandler.OnNetResponseCallBack mCallBack;
    @Inject
    CommentRepository mCommentRepository;

    @Override
    public void setListener(BackgroundTaskHandler.OnNetResponseCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void handleCommentInBackGroud(ICommentBean comment) {
        CommonMetadata commentBean = comment.get$$Comment();
        deleteComment(commentBean);
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put(BackgroundTaskHandler.NET_CALLBACK,mCallBack);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(commentBean.getString(CommonMetadata.METADATA_KEY_DELETE_URL));
        BackgroundTaskManager.getInstance(BaseApplication.getContext()).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public void handleComment(ICommentBean comment) {
        CommonMetadata commentBean = comment.get$$Comment();
    }

    protected void deleteComment(CommonMetadata commentBean) {

    }
}

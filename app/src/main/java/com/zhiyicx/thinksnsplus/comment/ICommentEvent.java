package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:25
 * @Email Jliuer@aliyun.com
 * @Description 评论处理的基类
 */
public interface ICommentEvent<C> {
    void handleCommentInBackGroud(C comment);
    void handleComment(C comment);
    void setListener(BackgroundTaskHandler.OnNetResponseCallBack callBack);
}

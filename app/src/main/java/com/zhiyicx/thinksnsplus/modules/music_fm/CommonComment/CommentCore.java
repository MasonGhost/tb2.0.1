package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:46
 * @Email Jliuer@aliyun.com
 * @Description 评论处理类
 */
public class CommentCore implements ICommentBean {
    private static final ICommentEvent SENDCOMMENT = new SendComment();
    private static final ICommentEvent DELETECOMMENT = new DeleteComment();
    private ICommentEvent defaultSate = SENDCOMMENT;
    private static CommentCore sCommentCore;
    private CommonMetadata mCommentBean;


    private CommentCore() {

    }

    public static CommentCore getInstance(CommentState sate,CallBack callBack) {
        if (sCommentCore == null) {
            synchronized (CommentCore.class) {
                if (sCommentCore == null) {
                    SENDCOMMENT.setListener(callBack);
                    DELETECOMMENT.setListener(callBack);
                    sCommentCore = new CommentCore();
                }
            }
        }
        sCommentCore.setDefaultSate(sate.mCommentEvent);
        return sCommentCore;
    }

    private void setDefaultSate(ICommentEvent defaultSate) {
        this.defaultSate = defaultSate;
    }

    public void handleComment() {
        if (mCommentBean == null) {
            throw new IllegalArgumentException("The CommonMetadata"
                    + " cannot be null");
        }
        CommentCore core = this;
        defaultSate.handleComment(core);
    }

    public void handleCommentInBackGroud() {
        if (mCommentBean == null) {
            throw new IllegalArgumentException("The CommonMetadata"
                    + " cannot be null");
        }
        CommentCore core = this;
        defaultSate.handleCommentInBackGroud(core);
    }

    public static class CallBack implements BackgroundTaskHandler.OnNetResponseCallBack,Serializable{
        @Override
        public void onSuccess(Object data) {
            LogUtils.d("--------------------------------------------------------------");
        }

        @Override
        public void onFailure(String message, int code) {

        }

        @Override
        public void onException(Throwable throwable) {

        }
    }

    @Override
    public CommentCore set$$Comment(CommonMetadata comment) {
        mCommentBean = comment;
        return sCommentCore;
    }

    @Override
    public CommonMetadata get$$Comment() {
        return mCommentBean;
    }

    public enum CommentState {
        SEND(SENDCOMMENT), DELETE(DELETECOMMENT);

        private ICommentEvent mCommentEvent;

        CommentState(ICommentEvent commentEvent) {
            mCommentEvent = commentEvent;
        }

    }

}


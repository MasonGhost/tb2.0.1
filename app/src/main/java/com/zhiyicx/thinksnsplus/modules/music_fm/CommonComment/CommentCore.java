package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:46
 * @Email Jliuer@aliyun.com
 * @Description  评论处理类
 */
public class CommentCore implements ICommentBean {

    private static final ICommentEvent SENDCOMMENT = new SendComment();
    private static final ICommentEvent DELETECOMMENT = new DeleteComment();
    private ICommentEvent defaultSate = SENDCOMMENT;
    private static CommentCore sCommentCore;
    private CommentBean mCommentBean;

    private CommentCore() {

    }

    public static CommentCore getInstance(CommentState sate) {
        if (sCommentCore == null) {
            synchronized (CommentCore.class) {
                if (sCommentCore == null) {
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
        if (mCommentBean==null){
            return;
        }
        CommentCore core = this;
        defaultSate.handleComment(core);
    }

    @Override
    public CommentCore set$$Comment(CommentBean comment) {
        mCommentBean = comment;
        return sCommentCore;
    }

    @Override
    public CommentBean get$$Comment() {
        return mCommentBean;
    }

    public enum CommentState{
        SEND(SENDCOMMENT),DELETE(DELETECOMMENT);

        private ICommentEvent mCommentEvent;

        CommentState(ICommentEvent commentEvent) {
            mCommentEvent = commentEvent;
        }
    }
}


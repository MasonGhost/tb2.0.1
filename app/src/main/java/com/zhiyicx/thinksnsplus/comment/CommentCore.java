package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:46
 * @Email Jliuer@aliyun.com
 * @Description 评论处理类,这个版本过时了哦，现在用 CommentCore_
 */
@Deprecated
public class CommentCore implements ICommentBean {

    private static final ICommentEvent SENDCOMMENT = new SendComment();
    private static final ICommentEvent DELETECOMMENT = new DeleteComment();
    private ICommentEvent defaultSate = SENDCOMMENT;
    private static CommentCore sCommentCore;
    private static CommonMetadata mCommentBean;
    private static CommonMetadataProvider mCommonMetadataProvider;

    private CommentCore() {

    }

    public static CommentCore getInstance(ICommentState sate, BackgroundTaskHandler.OnNetResponseCallBack callBack) {
        if (sCommentCore == null) {
            synchronized (CommentCore.class) {
                if (sCommentCore == null) {
                    SENDCOMMENT.setListener(callBack);
                    DELETECOMMENT.setListener(callBack);

                    if (callBack == null) {
                        SENDCOMMENT.setListener(new SendCallBack());
                    }

                    sCommentCore = new CommentCore();
                }
            }
        }
        sCommentCore.setDefaultSate(sate.getICommentEvent());
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

    /**
     * !!!弃用，该方法尚未完成扩展!!!
     *
     * @param comment
     * @return
     */
    @Override
    @Deprecated
    public CommentCore set$$Comment(CommonMetadata comment) {
        mCommentBean = comment;
        return sCommentCore;
    }

    public <C> CommentCore set$$Comment_(C comment, CommonMetadataProvider mMetadataProvider) {
        mCommentBean = mMetadataProvider.buildCommonMetadata(comment);
        mCommonMetadataProvider = mMetadataProvider;
        if (defaultSate == DELETECOMMENT) {
            mMetadataProvider.deleteOne(mMetadataProvider.buildCommonMetadataBean(comment));
        } else if (defaultSate == SENDCOMMENT) {
            mMetadataProvider.insertOrReplaceOne(mMetadataProvider.buildCommonMetadataBean(comment));
        }
        return sCommentCore;
    }

    @Override
    public CommonMetadata get$$Comment() {
        return mCommentBean;
    }

    // 可以自己实现 ICommentState 来扩展
    public enum CommentState implements ICommentState {
        SEND(SENDCOMMENT), DELETE(DELETECOMMENT);

        private ICommentEvent mCommentEvent;

        CommentState(ICommentEvent commentEvent) {
            mCommentEvent = commentEvent;
        }

        @Override
        public ICommentEvent getICommentEvent() {
            return mCommentEvent;
        }
    }

    public static class SendCallBack implements BackgroundTaskHandler.OnNetResponseCallBack, Serializable {
        private static final long serialVersionUID = -5469697109868235190L;

        @Override
        public void onSuccess(Object data) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_id(((Double) data).intValue());
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
            commonMetadataBean.setDelete_url(String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commonMetadataBean.getComment_id()));
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
            LogUtils.d("SendCallBack:::","onSuccess");
        }

        @Override
        public void onFailure(String message, int code) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
            LogUtils.d("SendCallBack:::","onFailure");
        }

        @Override
        public void onException(Throwable throwable) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
            LogUtils.d("SendCallBack:::","onException");
        }
    }

}


package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:46
 * @Email Jliuer@aliyun.com
 * @Description 评论处理类
 */
public class CommentCore_ implements ICommentBean {

    private static ICommentEvent SENDCOMMENT = new SendComment();
    private static ICommentEvent DELETECOMMENT = new DeleteComment();
    private ICommentState mICommentState;
    private CommonMetadata mCommentBean;
    private CommonMetadataProvider mCommonMetadataProvider;
    private BackgroundTaskHandler.OnNetResponseCallBack mCallBack;

    private CommentCore_(Builder builder) {
        mICommentState = builder.mICommentState;
        mCommentBean = builder.mCommonMetadata;
        mCommonMetadataProvider = builder.mCommonMetadataProvider;
        mCallBack = builder.mCallBack;
        if (mCallBack == null) {
           // mCallBack = new SendCallBack();
        }
        mICommentState.getICommentEvent().setListener(mCallBack);
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        ICommentState mICommentState;
        CommonMetadata mCommonMetadata;
        CommonMetadataProvider mCommonMetadataProvider;
        BackgroundTaskHandler.OnNetResponseCallBack mCallBack;

        public Builder() {

        }

        private Builder(CommentCore_ commentCore) {
            this.mICommentState = commentCore.mICommentState;
            this.mCommonMetadata = commentCore.mCommentBean;
            this.mCommonMetadataProvider = commentCore.mCommonMetadataProvider;
            this.mCallBack = commentCore.mCallBack;
        }

        public Builder buildCommentEvent(@NotNull ICommentState commentState) {
            this.mICommentState = commentState;
            return this;
        }

        public <C> Builder buildCommonMetadataAndProvider(@NotNull CommonMetadataProvider commonMetadataProvider, C comment) {
            this.mCommonMetadataProvider = commonMetadataProvider;
            this.mCommonMetadata = commonMetadataProvider.buildCommonMetadata(comment);
            return this;
        }

        public Builder buildCallBack(BackgroundTaskHandler.OnNetResponseCallBack callBack) {
            this.mCallBack = callBack;
            return this;
        }

        public CommentCore_ build() {
            if (mICommentState == null || mCommonMetadata == null || mCommonMetadataProvider == null) {
                throw new IllegalArgumentException("can not be null");
            }
            return new CommentCore_(this);
        }
    }

    @Override
    public CommonMetadata get$$Comment() {
        return mCommentBean;
    }

    @Override
    public ICommentBean set$$Comment(CommonMetadata comment) {
        return null;
    }

    public void handleComment() {
        if (mCommentBean == null) {
            throw new IllegalArgumentException("The CommonMetadata"
                    + " cannot be null");
        }
        CommentCore_ core = this;
        mICommentState.getICommentEvent().handleComment(core);
    }

    public void handleCommentInBackGroud() {
        if (mCommentBean == null) {
            throw new IllegalArgumentException("The CommonMetadata"
                    + " cannot be null");
        }
        CommentCore_ core = this;
        mICommentState.getICommentEvent().handleCommentInBackGroud(core);
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

    public class SendCallBack implements BackgroundTaskHandler.OnNetResponseCallBack, Serializable {
        private static final long serialVersionUID = -8811472258140739693L;

        @Override
        public void onSuccess(Object data) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_id(((Double) data).intValue());
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
            commonMetadataBean.setDelete_url(String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commonMetadataBean.getComment_id()));
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
        }

        @Override
        public void onFailure(String message, int code) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
        }

        @Override
        public void onException(Throwable throwable) {
            CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(mCommentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
            commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
            mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
        }
    }

}


package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/16:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TCommonMetadataProvider extends CommonMetadataProvider<MusicCommentListBean> {

    public TCommonMetadataProvider(List<MusicCommentListBean> comments) {
        super(comments);
    }

    @Override
    protected CommonMetadata buildCommonMetadata(MusicCommentListBean commentData) {
        return new CommonMetadata.Builder()
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_ID, commentData.getId().intValue())
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_STATE, commentData.getState())
                .putLong(CommonMetadata.METADATA_KEY_COMMENT_MARK, commentData.getComment_mark())
                .putString(CommonMetadata.METADATA_KEY_COMMENT_CONTENT, commentData.getComment_content())
                .putString(CommonMetadata.METADATA_KEY_CREATED_DATE, commentData.getCreated_at())
                .build();
    }

    @Override
    protected CommonMetadataBean buildCommonMetadataBean(MusicCommentListBean commentData) {
        CommonMetadataBean commonMetadataBean = new CommonMetadataBean();
        commonMetadataBean.setComment_mark(commentData.getComment_mark());
        commonMetadataBean.setComment_content(commentData.getComment_content());
        commonMetadataBean.setComment_id(commentData.getId().intValue());
        commonMetadataBean.setSource_id(commentData.getMusic_id() != 0 ? commentData.getMusic_id() : commentData.getSpecial_id());
        commonMetadataBean.setTo_user(commentData.getReply_to_user_id());
        commonMetadataBean.setFrom_user(commentData.getUser_id());
        commonMetadataBean.setComment_type(commentData.getMusic_id() == 0 ?CommentTypeConfig.TS_SPECIAL_COMMENT:CommentTypeConfig.TS_MUSIC_COMMENT);
        commonMetadataBean.setCreated_at(commentData.getCreated_at());
        commonMetadataBean.setUpdated_at(commentData.getUpdated_at());
        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
        return commonMetadataBean;
    }
}

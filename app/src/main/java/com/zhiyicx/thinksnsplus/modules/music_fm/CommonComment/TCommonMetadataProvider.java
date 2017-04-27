package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.baseproject.config.ApiConfig;
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
    public CommonMetadata buildCommonMetadata(MusicCommentListBean commentData) {
        return new CommonMetadata.Builder()
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_ID, commentData.getId() == null ? -1 : commentData.getId().intValue())
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_STATE, CommonMetadata.SEND_SUCCESS)
                .putLong(CommonMetadata.METADATA_KEY_COMMENT_MARK, commentData.getComment_mark())
                .putString(CommonMetadata.METADATA_KEY_COMMENT_CONTENT, commentData.getComment_content())
                .putString(CommonMetadata.METADATA_KEY_CREATED_DATE, commentData.getCreated_at())
                .putString(CommonMetadata.METADATA_KEY_COMMENT_URL, commentData.getMusic_id() == 0 ?
                        String.format(ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT, commentData.getSpecial_id()) :
                        String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT, commentData.getMusic_id()))
                .putString(CommonMetadata.METADATA_KEY_DELETE_URL, commentData.getMusic_id() == 0 ?
                        String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getSpecial_id()) :
                        String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getMusic_id()))
                .putObj(CommonMetadata.METADATA_KEY_TO_USER, commentData.getToUserInfoBean())
                .putObj(CommonMetadata.METADATA_KEY_FROM_USER, commentData.getFromUserInfoBean())
                .build();
    }

    @Override
    public CommonMetadataBean buildCommonMetadataBean(MusicCommentListBean commentData) {
        CommonMetadataBean commonMetadataBean = new CommonMetadataBean();
        commonMetadataBean.setComment_mark(commentData.getComment_mark());
        commonMetadataBean.setComment_content(commentData.getComment_content());
        commonMetadataBean.setComment_id(commentData.getId() == null ? -1 : commentData.getId().intValue());
        commonMetadataBean.setSource_id(commentData.getMusic_id() != 0 ? commentData.getMusic_id() : commentData.getSpecial_id());
        commonMetadataBean.setTo_user(commentData.getReply_to_user_id());
        commonMetadataBean.setFrom_user(commentData.getUser_id());
        commonMetadataBean.setComment_type(commentData.getMusic_id() == 0 ?
                CommentTypeConfig.TS_SPECIAL_COMMENT : CommentTypeConfig.TS_MUSIC_COMMENT);
        commonMetadataBean.setComment_url(commentData.getMusic_id() == 0 ?
                String.format(ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT, commentData.getSpecial_id()) :
                String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT, commentData.getMusic_id()));
        commonMetadataBean.setDelete_url(commentData.getMusic_id() == 0 ?
                String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getSpecial_id()) :
                String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getMusic_id()));
        commonMetadataBean.setCreated_at(commentData.getCreated_at());
        commonMetadataBean.setUpdated_at(commentData.getUpdated_at());
        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
        return commonMetadataBean;
    }

    @Override
    public MusicCommentListBean buildCommonData(CommonMetadataBean commonMetadataBean) {
        return null;
    }

    @Override
    public void saveCommonMetadataList(List<CommonMetadataBean> commonComments) {
        mCommonMetadataBeanGreenDao.saveMultiData(commonComments);
    }
}

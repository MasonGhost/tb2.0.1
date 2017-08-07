package com.zhiyicx.thinksnsplus.comment;

import android.app.Application;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/16:58
 * @Email Jliuer@aliyun.com
 * @Description 示例，音乐评论处理
 */
public class TCommonMetadataProvider extends CommonMetadataProvider<MusicCommentListBean> {
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    public TCommonMetadataProvider(List<MusicCommentListBean> comments) {
        super(comments);
        mUserInfoBeanGreenDao = new UserInfoBeanGreenDaoImpl((Application) BaseApplication.getContext());
    }

    @Override
    public CommonMetadata buildCommonMetadata(MusicCommentListBean commentData) {
        return new CommonMetadata.Builder()
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_ID, commentData.getId() == null ? -1 : commentData.getId().intValue())
                .putInteger(CommonMetadata.METADATA_KEY_COMMENT_STATE, mStates[commentData.getState()])
                .putInteger(CommonMetadata.METADATA_KEY_TO_USER_ID, (int)commentData.getReply_user())
                .putLong(CommonMetadata.METADATA_KEY_COMMENT_MARK, commentData.getComment_mark())
                .putString(CommonMetadata.METADATA_KEY_COMMENT_CONTENT, commentData.getComment_content())
                .putString(CommonMetadata.METADATA_KEY_CREATED_DATE, commentData.getCreated_at())
                .putString(CommonMetadata.METADATA_KEY_COMMENT_URL, commentData.getMusic_id() == 0 ?
                        String.format(ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT, commentData.getSpecial_id()) :
                        String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT, commentData.getMusic_id()))
                .putString(CommonMetadata.METADATA_KEY_DELETE_URL, String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getId()))
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
        commonMetadataBean.setTo_user((int)commentData.getReply_user());
        commonMetadataBean.setFrom_user((int)commentData.getUser_id());
        commonMetadataBean.setComment_type(commentData.getMusic_id() == 0 ?
                CommentTypeConfig.TS_SPECIAL_COMMENT : CommentTypeConfig.TS_MUSIC_COMMENT);
        commonMetadataBean.setComment_url(commentData.getMusic_id() == 0 ?
                String.format(ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT, commentData.getSpecial_id()) :
                String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT, commentData.getMusic_id()));
        commonMetadataBean.setDelete_url(String.format(ApiConfig.APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, commentData.getId()));
        commonMetadataBean.setCreated_at(commentData.getCreated_at());
        commonMetadataBean.setUpdated_at(commentData.getUpdated_at());
        commonMetadataBean.setComment_state(mStates[commentData.getState()]);
        return commonMetadataBean;
    }

    @Override
    public MusicCommentListBean buildRealNeedData(CommonMetadataBean commonMetadataBean) {
        MusicCommentListBean musicCommentListBean = new MusicCommentListBean();
        musicCommentListBean.setId((long)commonMetadataBean.getComment_id());
        musicCommentListBean.setUser_id(commonMetadataBean.getFrom_user());
        musicCommentListBean.setReply_user(commonMetadataBean.getTo_user());
        musicCommentListBean.setComment_content(commonMetadataBean.getComment_content());
        musicCommentListBean.setComment_mark(commonMetadataBean.getComment_mark());
        musicCommentListBean.setCreated_at(commonMetadataBean.getCreated_at());
        musicCommentListBean.setUpdated_at(commonMetadataBean.getUpdated_at());
        musicCommentListBean.setMusic_id(commonMetadataBean.getComment_type() ==
                CommentTypeConfig.TS_MUSIC_COMMENT ? commonMetadataBean.getSource_id() : 0);
        musicCommentListBean.setSpecial_id(commonMetadataBean.getComment_type() ==
                CommentTypeConfig.TS_SPECIAL_COMMENT ? commonMetadataBean.getSource_id() : 0);
        musicCommentListBean.setId((long) commonMetadataBean.getComment_id());
        musicCommentListBean.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                (long) commonMetadataBean.getFrom_user()));
        musicCommentListBean.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                (long) commonMetadataBean.getTo_user()));
        return musicCommentListBean;
    }
}

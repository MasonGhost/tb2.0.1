package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.squareup.leakcanary.LeakTraceElement;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.GroupDynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentHeader;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_FEED;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_GROUP_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_MUSIC;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_MUSIC_SPECIALS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_NEWS;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_ABLUM;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/13
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentAdapter extends CommonAdapter<CommentedBean> {
    public static final String BUNDLE_SOURCE_ID = "source_id";

    private ImageLoader mImageLoader;

    public MessageCommentAdapter(Context context, int layoutId, List<CommentedBean> datas) {
        super(context, layoutId, datas);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();

    }

    @Override
    protected void convert(final ViewHolder holder, final CommentedBean commentedBean, final int position) {

        ImageUtils.loadCircleUserHeadPic(commentedBean.getCommentUserInfo(), holder.getView(R.id.iv_headpic));

        if (commentedBean.getTarget_image() != null && commentedBean.getTarget_image() > 0) {
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(ImageUtils.imagePathConvertV2(commentedBean.getTarget_image().intValue()
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , ImageZipConfig.IMAGE_50_ZIP))
                    .imagerView(holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
        }

        if (commentedBean.getIsDelete()) {
            holder.getView(R.id.fl_detial).setVisibility(View.GONE);
            TextView review_flag = holder.getTextView(R.id.tv_review);
            review_flag.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.message_badge_bg));
            // 评论
            int resourceRes = 0;
            switch (commentedBean.getChannel()) {
                case APP_LIKE_FEED:
                    resourceRes = R.string.rank_dynamic;

                    break;
                case APP_LIKE_GROUP_POST:
                    resourceRes = R.string.post;
                    break;
                case APP_LIKE_MUSIC:
                    resourceRes = R.string.single_music;
                    break;
                case APP_LIKE_MUSIC_SPECIALS:
                    resourceRes = R.string.music_album;
                    break;
                case APP_LIKE_NEWS:
                    resourceRes = R.string.collect_info;

                    break;
                case ApiConfig.APP_QUESTIONS:
                    resourceRes = R.string.question;

                    break;
                case ApiConfig.APP_QUESTIONS_ANSWER:
                    resourceRes = R.string.draft_type_answers;
                    break;
                default:
            }
            if (resourceRes != 0) {
                review_flag.setText(holder.itemView.getResources().getString(R.string.resource_deleted_format,
                        holder.itemView.getResources().getString(resourceRes)));
            }
        } else {
            holder.getView(R.id.fl_detial).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_deatil, commentedBean.getTarget_title());
        }
        holder.setTextColorRes(R.id.tv_name, R.color.normal_for_assist_text);
        holder.setText(R.id.tv_name, handleName(commentedBean, holder));
        List<Link> links = setLiknks(holder, commentedBean);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_name), links);
        }

        holder.setText(R.id.tv_content, commentedBean.getComment_content());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(commentedBean.getUpdated_at()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(commentedBean.getCommentUserInfo()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(commentedBean.getCommentUserInfo()));

        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toDetail(commentedBean));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(holder.getConvertView(), holder, position);
                    }
                });
    }

    private List<Link> setLiknks(ViewHolder holder, final CommentedBean commentedBean) {
        List<Link> links = new ArrayList<>();
        Link nameLink = new Link(commentedBean.getCommentUserInfo().getName())
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  //
                // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional,
                // defaults to holo blue
                .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setOnClickListener((clickedText, linkMetadata) -> {
                    // single clicked
                    toUserCenter(commentedBean.getCommentUserInfo());
                });
        links.add(nameLink);
        if (commentedBean.getReplyUserInfo() != null && commentedBean.getReply_user() != null && commentedBean.getReply_user() != 0 &&
                commentedBean.getReplyUserInfo().getName() != null) {
            Link replyNameLink = new Link(commentedBean.getReplyUserInfo().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  //
                    // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) //
                    // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        toUserCenter(commentedBean.getReplyUserInfo());
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String handleName(CommentedBean commentedBean, ViewHolder holder) {
        String result;
        if (commentedBean.getReply_user() != null && commentedBean.getReply_user() != 0) { // 回复
            if (AppApplication.getMyUserIdWithdefault() == commentedBean.getReply_user()) {
                result = getContext().getResources().getString(R.string.comment_format_reply_you, commentedBean.getCommentUserInfo().getName());
            } else {
                result = getContext().getResources().getString(R.string.comment_format_reply, commentedBean.getCommentUserInfo().getName(),
                        commentedBean.getReplyUserInfo().getName());
            }
            return result;
        }

        // 评论
        switch (commentedBean.getChannel()) {
            case APP_LIKE_FEED:
                result = getContext().getResources().getString(R.string.comment_format_feed, commentedBean.getCommentUserInfo().getName());
                break;
            case APP_LIKE_GROUP_POST:
                result = getContext().getResources().getString(R.string.comment_format_group_feed, commentedBean.getCommentUserInfo().getName());
                break;
            case APP_LIKE_MUSIC:
            case APP_LIKE_MUSIC_SPECIALS:
                result = getContext().getResources().getString(R.string.comment_format_music, commentedBean.getCommentUserInfo().getName());

                break;
            case APP_LIKE_NEWS:
                result = getContext().getResources().getString(R.string.comment_format_news, commentedBean.getCommentUserInfo().getName());

                break;
            case ApiConfig.APP_QUESTIONS:
                result = getContext().getResources().getString(R.string.comment_format_questions, commentedBean.getCommentUserInfo().getName());

                break;
            case ApiConfig.APP_QUESTIONS_ANSWER:
                result = getContext().getResources().getString(R.string.comment_format_questions_answer, commentedBean.getCommentUserInfo().getName
                        ());

                break;
            default:
                result = "";
        }

        return result;
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(mContext, userInfoBean);
    }


    /**
     * 根据不同的type 进入不同的 详情
     *
     * @param commentedBean
     */
    private void toDetail(CommentedBean commentedBean) {
        if (commentedBean.getIsDelete()) {
            return;
        }
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, commentedBean.getTarget_id());
        switch (commentedBean.getChannel()) {

            case ApiConfig.APP_LIKE_FEED:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case ApiConfig.APP_LIKE_GROUP_POST:
                intent = new Intent(mContext, GroupDynamicDetailActivity.class);
                GroupDynamicListBean groupData = new Gson().fromJson(new Gson().toJson(commentedBean.getCommentable()), GroupDynamicListBean.class);
                bundle.putParcelable(DYNAMIC_DETAIL_DATA, groupData);
                bundle.putBoolean(LOOK_COMMENT_MORE, false);
                intent.putExtras(bundle);
                break;
            case ApiConfig.APP_LIKE_MUSIC:
                intent = new Intent(mContext, MusicCommentActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                MusicCommentHeader.HeaderInfo musicHeaderInfo = new MusicCommentHeader.HeaderInfo();
                try {
                    JSONObject jsonAlbum = new JSONObject(new Gson().toJson(commentedBean.getCommentable()));
                    musicHeaderInfo.setCommentCount(jsonAlbum.getInt("comment_count"));
                    musicHeaderInfo.setId(jsonAlbum.getInt("id"));
                    musicHeaderInfo.setTitle(jsonAlbum.getString("title"));
                    musicHeaderInfo.setLitenerCount(jsonAlbum.getInt("taste_count") + "");
                    musicHeaderInfo.setImageUrl(ImageUtils.imagePathConvertV2(jsonAlbum.getInt("storage")
                            , 40
                            , 40
                            , ImageZipConfig.IMAGE_100_ZIP));
                    bundle.putSerializable(CURRENT_COMMENT, musicHeaderInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;

            case ApiConfig.APP_LIKE_MUSIC_SPECIALS:
                intent = new Intent(mContext, MusicCommentActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_ABLUM);
                MusicCommentHeader.HeaderInfo albumHeaderInfo = new MusicCommentHeader.HeaderInfo();
                try {
                    JSONObject jsonAlbum = new JSONObject(new Gson().toJson(commentedBean.getCommentable()));
                    albumHeaderInfo.setCommentCount(jsonAlbum.getInt("comment_count"));
                    albumHeaderInfo.setId(jsonAlbum.getInt("id"));
                    albumHeaderInfo.setTitle(jsonAlbum.getString("title"));
                    albumHeaderInfo.setLitenerCount(jsonAlbum.getInt("taste_count") + "");
                    albumHeaderInfo.setImageUrl(ImageUtils.imagePathConvertV2(jsonAlbum.getInt("storage")
                            , 40
                            , 40
                            , ImageZipConfig.IMAGE_100_ZIP));
                    bundle.putSerializable(CURRENT_COMMENT, albumHeaderInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case ApiConfig.APP_LIKE_NEWS:
                intent = new Intent(mContext, InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;

            case ApiConfig.APP_QUESTIONS:
                intent = new Intent(mContext, QuestionDetailActivity.class);
                QAListInfoBean data = new Gson().fromJson(new Gson().toJson(commentedBean.getCommentable()), QAListInfoBean.class);
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, data);
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);

                break;
            case ApiConfig.APP_QUESTIONS_ANSWER:
                intent = new Intent(mContext, AnswerDetailsActivity.class);
                AnswerInfoBean answerInfoBean = new Gson().fromJson(new Gson().toJson(commentedBean.getCommentable()), AnswerInfoBean.class);
                bundle.putSerializable(BUNDLE_ANSWER, answerInfoBean);
                bundle.putLong(BUNDLE_SOURCE_ID, answerInfoBean.getId());
                intent.putExtras(bundle);
                break;
            default:
                return;

        }
        mContext.startActivity(intent);
    }

}

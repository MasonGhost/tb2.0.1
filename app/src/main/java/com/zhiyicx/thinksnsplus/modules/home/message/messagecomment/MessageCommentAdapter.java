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
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
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
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.GroupDynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_ABLUM;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;

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

        if (commentedBean.getTarget_image() != null) {
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
        if(commentedBean.getIsDelete()){
            holder.setText(R.id.tv_deatil, holder.getConvertView().getResources().getString(R.string.review_content_deleted));
        }else {
            holder.setText(R.id.tv_deatil, commentedBean.getTarget_title());
        }
        holder.setText(R.id.tv_name, commentedBean.getCommentUserInfo().getName());

        holder.setText(R.id.tv_content, setShowText(commentedBean));
        List<Link> links = setLiknks(holder, commentedBean);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }


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
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(holder.getConvertView(), holder, position);
                });
    }

    private List<Link> setLiknks(ViewHolder holder, final CommentedBean commentedBean) {
        List<Link> links = new ArrayList<>();
        if (commentedBean.getReplyUserInfo() != null && commentedBean.getReply_user() != null && commentedBean.getReply_user() != 0 && commentedBean.getReplyUserInfo().getName() != null) {
            Link replyNameLink = new Link(commentedBean.getReplyUserInfo().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnClickListener(clickedText -> {
                        // single clicked
                        toUserCenter(commentedBean.getReplyUserInfo());
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String setShowText(CommentedBean commentedBean) {
        return handleName(commentedBean);
    }

    private String handleName(CommentedBean commentedBean) {
        String content = "";
        if (commentedBean.getReply_user() != null && commentedBean.getReply_user() != 0) { // 当没有回复者时，就是回复评论
            content += "回复 " + commentedBean.getReplyUserInfo().getName() + ": " + commentedBean.getComment_content();
        } else {
            content = commentedBean.getComment_content();
        }
        return content;
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
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;

            case ApiConfig.APP_LIKE_MUSIC_SPECIALS:
                intent = new Intent(mContext, MusicCommentActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_ABLUM);
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

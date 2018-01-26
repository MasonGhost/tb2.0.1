package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_ABLUM;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/13
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeAdapter extends CommonAdapter<DigedBean> {
    private ImageLoader mImageLoader;
    private Drawable mLikeDrawable;

    public MessageLikeAdapter(Context context, int layoutId, List<DigedBean> datas) {
        super(context, layoutId, datas);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mLikeDrawable = UIUtils.getCompoundDrawables(context, R.mipmap.home_ico_good_high);
    }

    @Override
    protected void convert(final ViewHolder holder, final DigedBean digedBean, final int position) {
        ((TextView) holder.getView(R.id.tv_review)).setCompoundDrawables(null, null, mLikeDrawable, null);
        ImageUtils.loadCircleUserHeadPic(digedBean.getDigUserInfo(), holder.getView(R.id.iv_headpic));
        if (digedBean.getSource_cover() != null&&digedBean.getSource_cover()>0) {
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(ImageUtils.imagePathConvertV2(digedBean.getSource_cover().intValue()
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , ImageZipConfig.IMAGE_50_ZIP))
                    .imagerView(holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);

        }
        if (digedBean.getIsDelete()) {
            holder.setText(R.id.tv_deatil, holder.getConvertView().getResources().getString(R.string.review_content_deleted));
        } else {
            holder.setText(R.id.tv_deatil, digedBean.getSource_content());
        }
        holder.setVisible(R.id.tv_content, View.GONE);
        holder.setTextColorRes(R.id.tv_name, R.color.normal_for_assist_text);
        holder.setText(R.id.tv_name, handleName(digedBean));
        List<Link> links = setLiknks(holder, digedBean);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_name), links);
        }


        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(digedBean.getUpdated_at()));
        // 响应事件
//        RxView.clicks(holder.getView(R.id.tv_name))
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .subscribe(aVoid -> toUserCenter(digedBean.getDigUserInfo()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(digedBean.getDigUserInfo()));
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toDetail(digedBean));
    }

    private String handleName(DigedBean digedBean) {
        String result;
        switch (digedBean.getLikeable_type()) {
            case APP_LIKE_FEED:
                result = getContext().getResources().getString(R.string.digg_format_feed, digedBean.getDigUserInfo().getName());
                break;
            case APP_LIKE_GROUP_POST:
                result = getContext().getResources().getString(R.string.digg_format_group_feed, digedBean.getDigUserInfo().getName());
                break;
            case APP_LIKE_MUSIC:
            case APP_LIKE_MUSIC_SPECIALS:
                result = getContext().getResources().getString(R.string.digg_format_music, digedBean.getDigUserInfo().getName());

                break;
            case APP_LIKE_NEWS:
                result = getContext().getResources().getString(R.string.digg_format_news, digedBean.getDigUserInfo().getName());

                break;
            case ApiConfig.APP_QUESTIONS:
                result = getContext().getResources().getString(R.string.digg_format_questions, digedBean.getDigUserInfo().getName());

                break;
            case ApiConfig.APP_QUESTIONS_ANSWER:
                result = getContext().getResources().getString(R.string.digg_format_questions_answer, digedBean.getDigUserInfo().getName());

                break;
            default:
                result = "";
        }

        return result;
    }

    private List<Link> setLiknks(ViewHolder holder, DigedBean digedBean) {
        List<Link> links = new ArrayList<>();
        Link nameLink = new Link(digedBean.getDigUserInfo().getName())
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setOnClickListener((clickedText, linkMetadata) -> {
                    // single clicked
                    toUserCenter(digedBean.getDigedUserInfo());
                });
        links.add(nameLink);


        return links;

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
     * @param digedBean
     */
    private void toDetail(DigedBean digedBean) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, digedBean.getLikeable_id());
        switch (digedBean.getLikeable_type()) {

            case APP_LIKE_FEED:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case APP_LIKE_GROUP_POST:
                CirclePostListBean postListBean = new Gson().fromJson(new Gson().toJson(digedBean.getLikeable()), CirclePostListBean.class);
                intent = new Intent(mContext, CirclePostDetailActivity.class);
                bundle = new Bundle();
                bundle.putParcelable(CirclePostDetailFragment.POST, postListBean);
                bundle.putBoolean(CirclePostDetailFragment.BAKC2CIRCLE, true);
                bundle.putBoolean(CirclePostDetailFragment.LOOK_COMMENT_MORE, false);
                intent.putExtras(bundle);
                break;
            case APP_LIKE_MUSIC:
                intent = new Intent(mContext, MusicDetailActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case APP_LIKE_MUSIC_SPECIALS:
                intent = new Intent(mContext, MusicDetailActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_ABLUM);
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case APP_LIKE_NEWS:
                intent = new Intent(mContext, InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;
            case ApiConfig.APP_QUESTIONS:
                intent = new Intent(mContext, QuestionDetailActivity.class);
                QAListInfoBean data = new Gson().fromJson(new Gson().toJson(digedBean.getLikeable()), QAListInfoBean.class);
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, data);
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);

                break;
            case ApiConfig.APP_QUESTIONS_ANSWER:
                intent = new Intent(mContext, TopicDetailActivity.class);
                AnswerInfoBean answerInfoBean = new Gson().fromJson(new Gson().toJson(digedBean.getLikeable()), AnswerInfoBean.class);
                bundle.putSerializable(BUNDLE_ANSWER, answerInfoBean);
                bundle.putLong(BUNDLE_SOURCE_ID, answerInfoBean.getId());
                intent.putExtra(BUNDLE_TOPIC_BEAN, bundle);
                break;
            default:
                return;

        }
        mContext.startActivity(intent);
    }

}

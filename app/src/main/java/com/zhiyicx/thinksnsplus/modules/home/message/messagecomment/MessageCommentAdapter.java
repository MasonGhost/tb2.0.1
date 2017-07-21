package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;

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


        if (position == getItemCount() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }
        int storegeId;
        String userIconUrl;
        try {
            storegeId = Integer.parseInt(commentedBean.getCommentUserInfo().getAvatar());
            userIconUrl = ImageUtils.imagePathConvertV2(storegeId
                    , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                    , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                    , ImageZipConfig.IMAGE_38_ZIP);
        } catch (Exception e) {
            userIconUrl = commentedBean.getCommentUserInfo().getAvatar();
        }
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(userIconUrl)
                .transformation(new GlideCircleTransform(getContext()))
                .errorPic(R.mipmap.pic_default_portrait1)
                .placeholder(R.mipmap.pic_default_portrait1)
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        if (commentedBean.getTarget_image() != 0) {
            holder.setVisible(R.id.tv_deatil, View.GONE);
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(ImageUtils.imagePathConvertV2(commentedBean.getTarget_image()
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , ImageZipConfig.IMAGE_50_ZIP))
                    .imagerView(holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
            holder.setVisible(R.id.tv_deatil, View.VISIBLE);
            holder.setText(R.id.tv_deatil, commentedBean.getTarget_title());
        }

        holder.setText(R.id.tv_name, commentedBean.getCommentUserInfo().getName());

        holder.setText(R.id.tv_content, setShowText(commentedBean, position));
        List<Link> links = setLiknks(holder, commentedBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert((TextView) holder.getView(R.id.tv_content), links);
        }


        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(commentedBean.getUpdated_at()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(commentedBean.getCommentUserInfo());
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(commentedBean.getCommentUserInfo());
                    }
                });

        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toDetail(commentedBean);
                    }
                });
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClick(holder.getConvertView(), holder, position);
                    }
                });
    }

    private List<Link> setLiknks(ViewHolder holder, final CommentedBean commentedBean, int position) {
        List<Link> links = new ArrayList<>();
        if (commentedBean.getReplyUserInfo() != null && commentedBean.getReply_user() != null && commentedBean.getReply_user() != 0 && commentedBean.getReplyUserInfo().getName() != null) {
            Link replyNameLink = new Link(commentedBean.getReplyUserInfo().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            // single clicked
                            toUserCenter(commentedBean.getReplyUserInfo());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String setShowText(CommentedBean commentedBean, int position) {
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

            case ApiConfig.APP_COMPONENT_FEED:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case ApiConfig.APP_COMPONENT_MUSIC:
                intent = new Intent(mContext, MusicCommentActivity.class);
//                bundle.putString(CURRENT_COMMENT_TYPE, commentedBean.getSource_table().equals(APP_COMPONENT_SOURCE_TABLE_MUSICS) ? CURRENT_COMMENT_TYPE_MUSIC : CURRENT_COMMENT_TYPE_ABLUM);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case ApiConfig.APP_COMPONENT_NEWS:
                intent = new Intent(mContext, InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;
            default:
                return;

        }
        mContext.startActivity(intent);
    }

}

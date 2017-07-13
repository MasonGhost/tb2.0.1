package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
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

import static com.zhiyicx.baseproject.config.ApiConfig.APP_COMPONENT_SOURCE_TABLE_MUSICS;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean.TOP_REVIEWING;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_ABLUM;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;

/**
 * @Author Jliuer
 * @Date 2017/07/13/12:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MssageReviewAdapter extends CommonAdapter<TopDynamicCommentBean> {

    private ImageLoader mImageLoader;

    public MssageReviewAdapter(Context context, int layoutId, List<TopDynamicCommentBean> datas) {
        super(context, layoutId, datas);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
    }

    @Override
    protected void convert(ViewHolder holder, TopDynamicCommentBean
            topDynamicCommentBean, int position) {

        if (position == getItemCount() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }

        int storegeId;
        String userIconUrl;
        try {
            storegeId = Integer.parseInt(topDynamicCommentBean.getUserInfoBean().getAvatar());
            userIconUrl = ImageUtils.imagePathConvertV2(storegeId
                    , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                    , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                    , ImageZipConfig.IMAGE_38_ZIP);
        } catch (Exception e) {
            userIconUrl = topDynamicCommentBean.getUserInfoBean().getAvatar();
        }
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(userIconUrl)
                .transformation(new GlideCircleTransform(getContext()))
                .errorPic(R.mipmap.pic_default_portrait1)
                .placeholder(R.mipmap.pic_default_portrait1)
                .imagerView(holder.getView(R.id.iv_headpic))
                .build());

        holder.setVisible(R.id.iv_detail_image, View.GONE);
        holder.setVisible(R.id.tv_deatil, View.VISIBLE);
        holder.setText(R.id.tv_deatil, topDynamicCommentBean.getFeed().getContent());

        String content = String.format(getString(R.string.review_description), (float) topDynamicCommentBean.getAmount(),
                topDynamicCommentBean.getComment().getContent());

        TextView contentView = holder.getView(R.id.tv_content);
        TextView flagView = holder.getView(R.id.tv_review);
        contentView.setText(content);
        List<Link> links = setLiknks(holder, String.format(getString(R.string.dynamic_send_toll_select_money),
                (float) topDynamicCommentBean.getAmount()), topDynamicCommentBean.getComment().getContent());
        contentView.setLinksClickable(false);// 不能消费了点击事件啊
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(contentView, links);
        }
        flagView.setTextColor(SkinUtils.getColor(topDynamicCommentBean.getComment().isPinned()
                ? R.color.general_for_hint : R.color.dyanmic_top_flag));
        flagView.setText(getString(topDynamicCommentBean.getComment().isPinned() ? R.string.review_approved :
                (topDynamicCommentBean.getState() == TOP_REVIEWING ? R.string.review_ing : R.string.review_refuse)));

        holder.setText(R.id.tv_name, topDynamicCommentBean.getUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(topDynamicCommentBean.getCreated_at()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(topDynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(topDynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toDetail(topDynamicCommentBean));
    }

    private String getString(int resId){
        return mContext.getString(resId);
    }

    private List<Link> setLiknks(ViewHolder holder, final String money, final String comment) {
        List<Link> links = new ArrayList<>();
        Link moneyLink = new Link(money)
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_note))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)
                .setUnderlined(false);

        Link commentLink = new Link(comment)
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.normal_for_disable_button_text))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)
                .setUnderlined(false);

        links.add(moneyLink);
        links.add(commentLink);
        return links;
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(mContext, userInfoBean);
    }

    private void toDetail(TopDynamicCommentBean topDynamicCommentBean) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, topDynamicCommentBean.getFeed().getId());
        intent = new Intent(mContext, DynamicDetailActivity.class);
        intent.putExtras(bundle);
//        switch (commentedBean.getComponent()) {
//            case ApiConfig.APP_COMPONENT_FEED:
//                intent = new Intent(mContext, DynamicDetailActivity.class);
//                intent.putExtras(bundle);
//                break;
//            case ApiConfig.APP_COMPONENT_MUSIC:
//                intent = new Intent(mContext, MusicCommentActivity.class);
//                bundle.putString(CURRENT_COMMENT_TYPE, commentedBean.getSource_table().equals(APP_COMPONENT_SOURCE_TABLE_MUSICS) ? CURRENT_COMMENT_TYPE_MUSIC : CURRENT_COMMENT_TYPE_ABLUM);
//                intent.putExtra(CURRENT_COMMENT, bundle);
//                break;
//            case ApiConfig.APP_COMPONENT_NEWS:
//                intent = new Intent(mContext, InfoDetailsActivity.class);
//                intent.putExtra(BUNDLE_INFO, bundle);
//                break;
//            default:
//                return;
//
//        }
        mContext.startActivity(intent);
    }
}

package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.PinnedBean;
import com.zhiyicx.thinksnsplus.data.beans.TSNotifyExtraBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean.TOP_REVIEWING;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;

/**
 * @Author Jliuer
 * @Date 2017/07/13/12:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MssageReviewAdapter extends CommonAdapter<TSPNotificationBean> {

    private Gson mGson;

    public MssageReviewAdapter(Context context, int layoutId, List<TSPNotificationBean> datas) {
        super(context, layoutId, datas);
        mGson = new Gson();
    }

    @Override
    protected void convert(ViewHolder holder, TSPNotificationBean
            tspNotificationBean, int position) {
        String jsonString = mGson.toJson(tspNotificationBean.getData().getExtra(), Map.class);
        TSNotifyExtraBean extraBean = mGson.fromJson(jsonString, TSNotifyExtraBean.class);
        if (position == getItemCount() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }
        ImageUtils.loadCircleUserHeadPic(extraBean.getUser(), holder.getView(R.id.iv_headpic));
        DynamicDetailBeanV2 feedBean = extraBean.getFeed();
        CommentedBean commentBean = extraBean.getComment();
        PinnedBean pinnedBean = extraBean.getPinned();
        boolean hasImage = feedBean != null && !feedBean.getImages().isEmpty();

        holder.setVisible(R.id.iv_detail_image, hasImage ? View.VISIBLE : View.GONE);
        holder.setVisible(R.id.tv_deatil, !hasImage ? View.VISIBLE : View.GONE);

        holder.setText(R.id.tv_deatil, feedBean == null ? getString(R.string.review_dynamic_deleted) : feedBean.getFeed_content());
        if (hasImage) {
            int w = mContext.getResources().getDimensionPixelOffset(R.dimen.rec_image_for_list);
            GlideUrl url = ImageUtils.imagePathConvertV2(true, feedBean.getImages().get(0).getFile(),
                    w, w, ImageZipConfig.IMAGE_50_ZIP, AppApplication.getTOKEN());
            Glide.with(mContext)
                    .load(url)
                    .override(w, w)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(holder.getImageViwe(R.id.iv_detail_image));
        }

        String content = String.format(getString(R.string.review_description), PayConfig.realCurrencyFen2Yuan((float) pinnedBean.getAmount()),
                commentBean == null ? " " : commentBean.getComment_content());

        TextView contentView = holder.getView(R.id.tv_content);
        TextView flagView = holder.getView(R.id.tv_review);
        contentView.setText(content);
        List<Link> links = setLiknks(holder, String.format(getString(R.string.dynamic_send_toll_select_money),
                PayConfig.realCurrencyFen2Yuan(pinnedBean.getAmount())), commentBean == null ? " " : commentBean.getComment_content());
        contentView.setLinksClickable(false);// 不能消费了点击事件啊
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(contentView, links);
        }

        flagView.setTextColor(SkinUtils.getColor((commentBean == null || feedBean == null)
                ? R.color.important_for_note : (pinnedBean.getExpires_at() != null ? R.color.general_for_hint : R.color.dyanmic_top_flag)));


        flagView.setText(getString((commentBean == null) ? R.string.review_comment_deleted :
                (pinnedBean.getState() == PinnedBean.TOP_SUCCESS ? R.string.review_approved : ((pinnedBean.getExpires_at() == null &&
                        pinnedBean.getState() == TOP_REVIEWING) ? R.string.review_ing : R.string.review_refuse))));
        if (feedBean == null) {
            flagView.setText("");
        }

        holder.setText(R.id.tv_name, extraBean.getUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(tspNotificationBean.getCreated_at()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(extraBean.getUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(extraBean.getUser()));

        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (feedBean == null) {
                        holder.getConvertView().performClick();
                        return;
                    }
                    toDetail(feedBean.getId());
                });
    }

    private String getString(int resId) {
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

    private void toDetail(Long id) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, id);
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

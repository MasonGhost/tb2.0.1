package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.PinnedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/09/11/10:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopDyanmicCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopDyanmicCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_message_review_list;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopDynamicCommentBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean topDynamicCommentBean, BaseListBean lastT, int position, int itemCounts) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) topDynamicCommentBean;
        holder.setVisible(R.id.fl_detial, View.GONE);
        ImageUtils.loadCircleUserHeadPic(dynamicCommentBean.getUserInfoBean(), holder.getView(R.id.iv_headpic));

        TextView review_flag = holder.getTextView(R.id.tv_review);
        if (dynamicCommentBean.getExpires_at() == null) {
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color.dyanmic_top_flag));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_ing));
        } else {

            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color.general_for_hint));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_done));

//            long nowTime = System.currentTimeMillis();
//            long expires_at = TimeUtils.utc2LocalLong(dynamicCommentBean.getExpires_at());
//            if (nowTime >= expires_at) {
//                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
//                review_flag.setText(holder.itemView.getResources().getString(R.string.review_refuse));
//            } else {
//
//            }
        }

        if (dynamicCommentBean.getFeed() != null && dynamicCommentBean.getFeed().getImages() != null
                && !dynamicCommentBean.getFeed().getImages().isEmpty()) {
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(ImageUtils.imagePathConvertV2(dynamicCommentBean.getFeed().getImages().get(0).getFile()
                            , holder.itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , holder.itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                            , ImageZipConfig.IMAGE_50_ZIP))
                    .into((ImageView) holder.getView(R.id.iv_detail_image));

        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
        }

        if (dynamicCommentBean.getFeed() == null || dynamicCommentBean.getComment() == null) {
            holder.setText(R.id.tv_deatil, holder.getConvertView().getResources().getString(R.string.review_content_deleted));
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
            review_flag.setText(holder.itemView.getResources().getString(dynamicCommentBean.getFeed() == null ?
                    R.string.review_dynamic_deleted : R.string.review_comment_deleted));
            holder.setText(R.id.tv_content, String.format(Locale.getDefault(),
                    holder.itemView.getContext().getString(R.string.stick_type_dynamic_commnet_message), " "));
        } else {
            holder.setVisible(R.id.fl_detial, View.GONE);
            holder.setText(R.id.tv_content, String.format(Locale.getDefault(),
                    holder.itemView.getContext().getString(R.string.stick_type_dynamic_commnet_message), TextUtils.isEmpty(dynamicCommentBean.getComment().getComment_content())
                            ? " " : dynamicCommentBean.getComment().getComment_content()));
            List<Link> links = setLinks(holder.itemView.getContext());
            if (!links.isEmpty()) {
                ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
            }
            holder.setText(R.id.tv_deatil, dynamicCommentBean.getFeed().getFeed_content());
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, dynamicCommentBean.getUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicCommentBean.getUpdated_at()));


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> holder.itemView.performClick());
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (dynamicCommentBean.getFeed() == null || dynamicCommentBean.getComment() == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(dynamicCommentBean.getComment());
                });

        RxView.clicks(review_flag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (dynamicCommentBean.getExpires_at() == null
                            && dynamicCommentBean.getFeed() != null
                            && dynamicCommentBean.getComment() != null)
                        initReviewPopWindow(dynamicCommentBean, position);
                });
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) data;
        dynamicCommentBean.getFeed().setPinned(PinnedBean.TOP_SUCCESS);
        dynamicCommentBean.setExpires_at(TimeUtils.millis2String(System.currentTimeMillis() + 1000000));
        BaseListBean result = dynamicCommentBean;
        mPresenter.approvedTopComment((long) dynamicCommentBean.getFeed().getId(),
                dynamicCommentBean.getComment().getId().intValue(), dynamicCommentBean.getId().intValue(), result, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) data;
        dynamicCommentBean.getFeed().setPinned(PinnedBean.TOP_REFUSE);
        dynamicCommentBean.setExpires_at(TimeUtils.getCurrenZeroTimeStr());
        mPresenter.refuseTopComment(dynamicCommentBean.getId().intValue(), data, position);
    }

}

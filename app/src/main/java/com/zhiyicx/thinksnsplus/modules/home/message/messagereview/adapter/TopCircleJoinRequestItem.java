package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopCircleJoinReQuestBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/12/13/9:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopCircleJoinRequestItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopCircleJoinRequestItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopCircleJoinReQuestBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) baseListBean;

        holder.setVisible(R.id.fl_detial, View.GONE);
        ImageUtils.loadCircleUserHeadPic(circleJoinReQuestBean.getUser(), holder.getView(R
                .id.iv_headpic));

        TextView review_flag = holder.getTextView(R.id.tv_review);
        if (circleJoinReQuestBean.getAudit() == TopCircleJoinReQuestBean.TOP_REVIEW) {
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .dyanmic_top_flag));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_ing));
        } else {
            if (circleJoinReQuestBean.getAudit() == TopCircleJoinReQuestBean.TOP_REFUSE) {
                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                        .message_badge_bg));
                review_flag.setText(holder.itemView.getResources().getString(R.string
                        .circle_report_disagree));
            } else {
                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                        .general_for_hint));
                review_flag.setText(holder.itemView.getResources().getString(R.string
                        .circle_report_agree));
            }
        }

        holder.setVisible(R.id.iv_detail_image, View.GONE);

        String commentBody = circleJoinReQuestBean.getGroup().getName();
        holder.setText(R.id.tv_content, String.format(Locale.getDefault(),
                holder.itemView.getContext().getString(R.string
                        .stick_type_group_join_message), TextUtils.isEmpty(commentBody) ? "" +
                        " " : commentBody));
        List<Link> links = setLinks(holder.itemView.getContext());
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, circleJoinReQuestBean.getUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(circleJoinReQuestBean
                .getUpdated_at()));


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        circleJoinReQuestBean.getUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        circleJoinReQuestBean.getUser()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.itemView.performClick());
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (circleJoinReQuestBean.getGroup() == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(circleJoinReQuestBean.getGroup());
                });

        RxView.clicks(review_flag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (circleJoinReQuestBean.getAudit() == 0) {
                        initReviewPopWindow(circleJoinReQuestBean, position);
                    }
                });
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) data;
        circleJoinReQuestBean.setAudit(TopCircleJoinReQuestBean.TOP_SUCCESS);
        mPresenter.approvedTopComment(circleJoinReQuestBean.getGroup_id(), circleJoinReQuestBean.getId().intValue(), 0, circleJoinReQuestBean, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) data;
        circleJoinReQuestBean.setAudit(TopCircleJoinReQuestBean.TOP_REFUSE);
        mPresenter.refuseTopComment(0, data, position);
    }

    @Override
    protected void toDetail(CommentedBean commentedBean) {
    }

    protected void toDetail(CircleInfo circleInfo) {
        Intent intent = new Intent(mContext, CircleDetailActivity.class);
        intent.putExtra(CircleDetailFragment.CIRCLE_ID, circleInfo.getId());
        mContext.startActivity(intent);
    }

    @Override
    protected void initReviewPopWindow(BaseListBean dataBean, int position) {
        mReviewPopWindow = ActionPopupWindow.builder()
                .item1Str(mContext.getString(R.string.circle_report_agree))
                .item2Str(mContext.getString(R.string.circle_report_disagree))
                .bottomStr(mContext.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with((Activity) mContext)
                .item1ClickListener(() -> {
                    onReviewApprovedClick(dataBean, position);
                    mReviewPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    onReviewRefuseClick(dataBean, position);
                    mReviewPopWindow.hide();
                })
                .bottomClickListener(() -> mReviewPopWindow.hide())
                .build();
        mReviewPopWindow.show();
    }
}

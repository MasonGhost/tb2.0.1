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
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/12/08/17:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopPostCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopPostCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopPostCommentListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int
            position, int itemCounts) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) baseListBean;

        holder.setVisible(R.id.fl_detial, View.GONE);
        ImageUtils.loadCircleUserHeadPic(postCommentListBean.getCommentUser(), holder.getView(R
                .id.iv_headpic));

        TextView review_flag = holder.getTextView(R.id.tv_review);
        if (postCommentListBean.getExpires_at() == null) {
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .dyanmic_top_flag));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_ing));
        } else {

            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color.general_for_hint));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_done));

//            long nowTime = System.currentTimeMillis();
//            long expires_at = TimeUtils.utc2LocalLong(postCommentListBean.getExpires_at());
//            if (postCommentListBean.getState() == TopNewsCommentListBean.TOP_REFUSE) {
//                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
//                        .message_badge_bg));
//                review_flag.setText(holder.itemView.getResources().getString(R.string
//                        .review_refuse));
//            } else {
//                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
//                        .general_for_hint));
//                review_flag.setText(holder.itemView.getResources().getString(R.string
//                        .review_approved));
//            }
        }


        if (postCommentListBean.getPost() != null && postCommentListBean.getPost().getImages() !=
                null
                && !postCommentListBean.getPost().getImages().isEmpty()) {
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(ImageUtils.imagePathConvertV2(postCommentListBean.getPost().getImages()
                                    .get(0).getFile_id()
                            , holder.itemView.getContext().getResources().getDimensionPixelOffset
                                    (R.dimen.headpic_for_user_center)
                            , holder.itemView.getContext().getResources().getDimensionPixelOffset
                                    (R.dimen.headpic_for_user_center)
                            , ImageZipConfig.IMAGE_50_ZIP))
                    .into((ImageView) holder.getView(R.id.iv_detail_image));

        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
        }

        if (postCommentListBean.getPost() == null || postCommentListBean.getComment() == null) {
            holder.setText(R.id.tv_deatil, holder.getConvertView().getResources().getString(R
                    .string.review_content_deleted));
            holder.setText(R.id.tv_content, String.format(Locale.getDefault(),
                    holder.itemView.getContext().getString(R.string
                            .stick_type_group_commnet_message), " "));
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .message_badge_bg));
            review_flag.setText(holder.itemView.getResources().getString(postCommentListBean
                    .getPost() == null ?
                    R.string.review_dynamic_deleted : R.string.review_comment_deleted));
        } else {
            String commentBody = RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT,
                    postCommentListBean.getComment().getComment_content());
            holder.setText(R.id.tv_content, String.format(Locale.getDefault(),
                    holder.itemView.getContext().getString(R.string
                            .stick_type_news_commnet_message), TextUtils.isEmpty(commentBody) ? "" +
                            " " : commentBody));
            List<Link> links = setLinks(holder.itemView.getContext());
            if (!links.isEmpty()) {
                ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
            }
            holder.setText(R.id.tv_deatil, postCommentListBean.getPost().getSummary());
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, postCommentListBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(postCommentListBean
                .getUpdated_at()));


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        postCommentListBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        postCommentListBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.itemView.performClick());
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (postCommentListBean.getPost() == null || postCommentListBean.getComment()
                            == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(postCommentListBean.getPost());
                });

        RxView.clicks(review_flag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (postCommentListBean.getExpires_at() == null
                            && postCommentListBean.getPost() != null
                            && postCommentListBean.getComment() != null) {
                        initReviewPopWindow(postCommentListBean, position);
                    }
                });
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) data;
        postCommentListBean.setExpires_at(TimeUtils.millis2String(System.currentTimeMillis() +
                1000000));
        postCommentListBean.setState(TopNewsCommentListBean.TOP_SUCCESS);
        BaseListBean result = postCommentListBean;
        mPresenter.approvedTopComment(0L,
                postCommentListBean.getComment().getId().intValue(), 0, result, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) data;
        postCommentListBean.setExpires_at(TimeUtils.getCurrenZeroTimeStr());
        postCommentListBean.setState(TopNewsCommentListBean.TOP_REFUSE);
        mPresenter.refuseTopComment(postCommentListBean.getComment().getId().intValue(), data, position);
    }

    @Override
    protected void toDetail(CommentedBean commentedBean) {
    }

    protected void toDetail(CirclePostListBean postListBean) {
        CirclePostDetailActivity.startActivity(mContext,postListBean);
    }
}

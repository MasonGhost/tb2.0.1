package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter.BaseTopItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.ExpandableTextView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/12/14/10:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleReportItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public static final String TYPEPOST = "post";
    public static final String TYPECOMMENT = "comment";

    private ReporReviewContract.Presenter mPresenter;
    private CenterInfoPopWindow mReportPopWindow;
    private Activity mActivity;

    public CircleReportItem(Activity activity, ReporReviewContract.Presenter presenter) {
        super(activity);
        mPresenter = presenter;
        mActivity = activity;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof CircleReportListBean;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_circle_report_list;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {
        CircleReportListBean circleReportListBean = (CircleReportListBean) baseListBean;
        CirclePostCommentBean commentBean = null;
        CirclePostListBean postListBean = null;
        boolean isPost, isComment, contentIsNull;
        long circleId, postId;

        isPost = TYPEPOST.equals(circleReportListBean.getType());
        isComment = TYPECOMMENT.equals(circleReportListBean.getType());
        if (isPost) {
            Gson gson = new Gson();
            postListBean = gson.fromJson(gson.toJson(circleReportListBean.getResource()), CirclePostListBean.class);
        } else if (isComment) {
            Gson gson = new Gson();
            commentBean = gson.fromJson(gson.toJson(circleReportListBean.getResource()), CirclePostCommentBean.class);
        }
        contentIsNull = (isPost && postListBean == null) || (isComment && commentBean == null);

        circleId = circleReportListBean.getGroup_id();
        postId = contentIsNull ? 0 : (isComment ? commentBean.getPost_id() : postListBean.getId());


        ImageUtils.loadCircleUserHeadPic(circleReportListBean.getUser(), holder.getView(R
                .id.iv_headpic));

        TextView review_flag = holder.getTextView(R.id.tv_review);
        review_flag.setVisibility(contentIsNull ? View.GONE : View.VISIBLE);
        if (circleReportListBean.getStatus() == CircleReportListBean.TOP_REVIEW) {
            review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .dyanmic_top_flag));
            review_flag.setText(holder.itemView.getResources().getString(R.string.review_ing));
        } else {
            if (circleReportListBean.getStatus() == CircleReportListBean.TOP_REFUSE) {
                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                        .message_badge_bg));
                review_flag.setText(holder.itemView.getResources().getString(R.string
                        .circle_report_refuse));
            } else {
                review_flag.setTextColor(holder.itemView.getResources().getColor(R.color
                        .general_for_hint));
                review_flag.setText(holder.itemView.getResources().getString(R.string
                        .circle_report_done));
            }
        }

        int detailIamgeId = -1;
        if (postListBean != null) {
            detailIamgeId = RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, postListBean.getBody());
        }
        ImageView detailImageView = holder.getImageViwe(R.id.iv_detail_image);
        detailImageView.setVisibility(detailIamgeId > 0 ? View.VISIBLE : View.GONE);
        if (detailIamgeId > 0) {
            Glide.with(mContext)
                    .load(ImageUtils.imagePathConvertV2(detailIamgeId, 0, 0, ImageZipConfig.IMAGE_80_ZIP))
                    .into(detailImageView);
        }

        ExpandableTextView contentView = holder.getView(R.id.tv_content);
        TextView detailContentView = holder.getTextView(R.id.tv_deatil);
        if (contentIsNull) {
            detailContentView.setText(R.string.review_content_deleted);
        } else {
            String detailContent = isPost ? postListBean.getSummary() : commentBean.getContent();
            detailContentView.setText(detailContent);
        }
        contentView.setText(circleReportListBean.getContent());
        contentView.setExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(ExpandableTextView view) {
                showRulePopupWindow(circleReportListBean.getContent(), view);
            }

            @Override
            public void onShrink(ExpandableTextView view) {

            }
        });

        String userName, targetName;
        userName = circleReportListBean.getUser().getName();
        targetName = circleReportListBean.getTarget().getName();
        String content = String.format(Locale.getDefault(),
                mContext.getString(isPost ? R.string.circle_report_post : R.string.circle_report_comment), userName
                , targetName);

        holder.setText(R.id.tv_name, content);

        holder.setTextColorRes(R.id.tv_name, R.color.normal_for_assist_text);
        List<Link> links = setLinks(mContext, circleReportListBean.getUser(), circleReportListBean.getTarget());
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_name), links, false);
        }

        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(circleReportListBean
                .getUpdated_at()));


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        circleReportListBean.getUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        circleReportListBean.getUser()));
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (contentIsNull) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(circleId, postId, isComment);
                });

        RxView.clicks(review_flag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (circleReportListBean.getStatus() == 0) {
                        initReviewPopWindow(circleReportListBean, position);
                    }
                });
    }

    private List<Link> setLinks(Context context, UserInfoBean name, UserInfoBean targetName) {
        List<Link> links = new ArrayList<>();
        Link userLink = new Link(name.getName())
                .setTextColor(ContextCompat.getColor(context, R.color.important_for_theme))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color
                        .important_for_theme))
                .setHighlightAlpha(.8f)
                .setOnClickListener((clickedText, linkMetadata) -> PersonalCenterFragment.startToPersonalCenter(mContext, name))
                .setUnderlined(false);
        links.add(userLink);

        Link targetLink = new Link(targetName.getName())
                .setTextColor(ContextCompat.getColor(context, R.color.important_for_theme))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color
                        .important_for_theme))
                .setHighlightAlpha(.8f)
                .setOnClickListener((clickedText, linkMetadata) -> PersonalCenterFragment.startToPersonalCenter(mContext, targetName))
                .setUnderlined(false);
        links.add(targetLink);
        return links;
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        CircleReportListBean circleReportListBean = (CircleReportListBean) data;
        circleReportListBean.setStatus(CircleReportListBean.TOP_SUCCESS);
        mPresenter.approvedCircleReport(circleReportListBean.getId());
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        CircleReportListBean circleReportListBean = (CircleReportListBean) data;
        circleReportListBean.setStatus(CircleReportListBean.TOP_REFUSE);
        mPresenter.refuseCircleReport(circleReportListBean.getId());
    }

    @Override
    protected void toDetail(CommentedBean commentedBean) {
    }

    protected void toDetail(long circleId, long postId, boolean isLookMoreComment) {
        CirclePostDetailActivity.startActivity(mContext, circleId, postId, isLookMoreComment, true);
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

    private void showRulePopupWindow(String report, View parent) {
        mReportPopWindow = CenterInfoPopWindow.builder()
                .titleStr(mActivity.getString(R.string.circle_report_reason))
                .desStr(report)
                .item1Str(mActivity.getString(R.string.close))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(mActivity)
                .buildCenterPopWindowItem1ClickListener(() -> mReportPopWindow.hide())
                .parentView(parent)
                .build();
        mReportPopWindow.show();
    }
}

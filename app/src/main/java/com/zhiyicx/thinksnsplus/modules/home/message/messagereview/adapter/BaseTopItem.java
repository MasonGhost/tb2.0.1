package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/09/11/14:20
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseTopItem implements ItemViewDelegate<BaseListBean> {

    public static final String BUNDLE_SOURCE_ID = "source_id";
    protected Context mContext;
    protected ActionPopupWindow mInstructionsPopupWindow;
    protected ActionPopupWindow mReviewPopWindow;
    protected TopReviewEvetnInterface mTopReviewEvetnInterface;
    protected MessageReviewContract.Presenter mPresenter;

    public BaseTopItem(Context context, MessageReviewContract.Presenter presenter) {
        mContext = context;
        this.mPresenter = presenter;
    }

    public BaseTopItem(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_message_review_list;
    }

    @Override
    public abstract boolean isForViewType(BaseListBean item, int position);

    @Override
    public abstract void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts);

    /**
     * 前往用户个人中心
     */
    protected void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

    /**
     * @param commentedBean
     */
    protected void toDetail(CommentedBean commentedBean) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, commentedBean.getTarget_id());
        switch (commentedBean.getChannel()) {
            case ApiConfig.APP_LIKE_FEED:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case ApiConfig.APP_LIKE_NEWS:
                intent = new Intent(mContext, InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;
            case ApiConfig.APP_LIKE_GROUP_POST:
                CirclePostListBean postListBean = new Gson().fromJson(new Gson().toJson(commentedBean.getCommentable()), CirclePostListBean.class);
                intent = new Intent(mContext, CirclePostDetailActivity.class);
                bundle = new Bundle();
                bundle.putParcelable(CirclePostDetailFragment.POST, postListBean);
                bundle.putBoolean(CirclePostDetailFragment.BAKC2CIRCLE, true);
                bundle.putBoolean(CirclePostDetailFragment.LOOK_COMMENT_MORE, true);
                intent.putExtras(bundle);
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

    protected List<Link> setLinks(Context context) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(Pattern.compile("“[\\s\\S]*”"))
                .setTextColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);
        return links;
    }

    protected void initInstructionsPop(int resDesStr) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .desStr(mContext.getString(resDesStr))
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(mContext.getString(R.string.instructions))
                .desStr(mContext.getString(resDesStr))
                .bottomStr(mContext.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with((Activity) mContext)
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

    protected void initReviewPopWindow(BaseListBean dataBean, int position) {
        mReviewPopWindow = ActionPopupWindow.builder()
                .item1Str(mContext.getString(R.string.review_approved))
                .item2Str(mContext.getString(R.string.review_refuse))
                .bottomStr(mContext.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with((Activity) mContext)
                .item1ClickListener(() -> {
                    if (mTopReviewEvetnInterface != null) {
                        mTopReviewEvetnInterface.onReviewApprovedClick(dataBean, position);
                    }
                    mReviewPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    if (mTopReviewEvetnInterface != null) {
                        mTopReviewEvetnInterface.onReviewRefuseClick(dataBean, position);
                    }
                    mReviewPopWindow.hide();
                })
                .bottomClickListener(() -> mReviewPopWindow.hide())
                .build();
        mReviewPopWindow.show();
    }

    public void setTopReviewEvetnInterface(TopReviewEvetnInterface topReviewEvetnInterface) {
        mTopReviewEvetnInterface = topReviewEvetnInterface;
    }

    public interface TopReviewEvetnInterface {
        void onReviewApprovedClick(BaseListBean data, int position);

        void onReviewRefuseClick(BaseListBean data, int position);
    }
}

package com.zhiyicx.thinksnsplus.modules.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.klinker.android.link_builder.Link;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscription;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 聚合举报
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportFragment extends TSFragment<ReportContract.Presenter> implements ReportContract.View {

    public static final String BUNDLE_REPORT_RESOURCE_DATA = "report_resource_data";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_des)
    TextView mTvDes;
    @BindView(R.id.iv_img)
    SquareImageView mIvImg;
    @BindView(R.id.ll_resource_contianer)
    LinearLayout mLlResourceContianer;
    @BindView(R.id.et_report_content)
    UserInfoInroduceInputView mEtReportContent;
    @BindView(R.id.bt_report)
    LoadingButton mBtReport;
    AnimationDrawable mLoginAnimationDrawable;


    private ReportResourceBean mReportResourceBean;

    public static ReportFragment newInstance(Bundle bundle) {
        ReportFragment reportFragment = new ReportFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.report);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            mReportResourceBean = (ReportResourceBean) getArguments().getSerializable(BUNDLE_REPORT_RESOURCE_DATA);
            initListener();
        } else {
            throw new IllegalArgumentException("params is error ！please check it.");
        }
    }

    @Override
    protected void initData() {

        if (TextUtils.isEmpty(mReportResourceBean.getTitle())) {
            mReportResourceBean.setTitle(getString(R.string.default_delete_user_name));
        }
        switch (mReportResourceBean.getType()) {
            case INFO:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.collect_info)));
                break;
            case DYNAMIC:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.collect_dynamic)));
                break;
            case QA:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.qa)));
                break;
            case QA_ANSWER:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.qa_answer)));
                break;
            case CIRCLE:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.circle)));
                break;
            case CIRCLE_POST:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.circle_post)));
                break;
            case COMMENT:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle(), getString(R.string.comment)));
                break;
            default:
        }
        // title 点击
        ConvertUtils.stringLinkConvert(mTvTitle, setLiknks(), true);

        if (!TextUtils.isEmpty(mReportResourceBean.getDes())) {
            mTvDes.setText(mReportResourceBean.getDes());
        }
        if (!TextUtils.isEmpty(mReportResourceBean.getImg())) {
            ImageUtils.loadImageDefault(mIvImg, mReportResourceBean.getImg());
        } else {
            mIvImg.setVisibility(View.GONE);
        }

    }

    @Override
    public void showLoading() {
        super.showLoading();
        mBtReport.handleAnimation(true);
        mBtReport.setEnabled(!TextUtils.isEmpty(mEtReportContent.getInputContent()));
        mLoginAnimationDrawable = mBtReport.getAnimationDrawable();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mBtReport.handleAnimation(false);
        mBtReport.setEnabled(!TextUtils.isEmpty(mEtReportContent.getInputContent()));
    }

    /**
     * 举报成功回调
     *
     * @param data
     */
    @Override
    public void reportSuccess(ReportResultBean data) {

        showSnackSuccessMessage(getString(R.string.report_success_tip));
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (mActivity != null && Prompt.SUCCESS == prompt) {
            mActivity.finish();
        }
    }

    private void initListener() {

        mEtReportContent.setBackgroundResource(R.drawable.shape_rect_bg_boder_gray);

        // 内容变化监听
        RxTextView.textChanges(mEtReportContent.getEtContent())
                .map(charSequence -> charSequence.toString().replaceAll(" ", "").length() > 0)
                .subscribe(aBoolean ->
                        mBtReport.setEnabled(aBoolean)
                );
        // 举报点击
        RxView.clicks(mBtReport)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mEtReportContent.getEtContent());
                    mPresenter.report(mEtReportContent.getInputContent(), mReportResourceBean);
                });
        // 资源点击
        RxView.clicks(mLlResourceContianer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    goDetail();

                });


    }

    private List<Link> setLiknks() {
        List<Link> links = new ArrayList<>();
        if (!TextUtils.isEmpty(mReportResourceBean.getTitle())) {
            Link link = new Link(mReportResourceBean.getTitle())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color
                            .themeColor))
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                            .themeColor))
                    .setHighlightAlpha(.8f)
                    .setUnderlined(false)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        goDetail();
                    });
            links.add(link);
        }
        return links;
    }

    /**
     * 跳转资源内容
     */
    private void goDetail() {
        DeviceUtils.hideSoftKeyboard(getContext(), mEtReportContent.getEtContent());
        switch (mReportResourceBean.getType()) {
            case INFO:
                break;

            case DYNAMIC:
                break;
            case QA:

                break;

            case QA_ANSWER:

                break;

            case CIRCLE:

                break;
            case CIRCLE_POST:

                break;
            case COMMENT:

                break;
            default:
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLoginAnimationDrawable != null && mLoginAnimationDrawable.isRunning()) {
            mLoginAnimationDrawable.stop();
        }
    }
}

package com.zhiyicx.thinksnsplus.modules.report;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.klinker.android.link_builder.Link;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.DefaultUserInfoConfig;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 聚合举报
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportFragment extends TSFragment<ReportContract.Presenter> implements ReportContract.View {

    /**
     * 当没有 title 时， 描述最多显示 2 行
     */
    private static final int DEFAULT_RESOURCE_DES_MAX_LINES = 2;

    public static final String BUNDLE_REPORT_RESOURCE_DATA = "report_resource_data";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_resource_title)
    TextView mTvResourceTitle;
    @BindView(R.id.tv_resource_des)
    TextView mTvResourceDes;
    @BindView(R.id.iv_img)
    SquareImageView mIvImg;
    @BindView(R.id.ll_resource_contianer)
    View mLlResourceContianer;
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
        if (mReportResourceBean.getUser() == null || mReportResourceBean.getUser().getUser_id() == null) {
            throw new IllegalArgumentException("user info not be null!");
        }
        if (TextUtils.isEmpty(mReportResourceBean.getUser().getName())) {
            mPresenter.getUserInfoById(mReportResourceBean.getUser().getUser_id());
        } else {
            updateData();
        }

    }

    private void updateData() {
        switch (mReportResourceBean.getType()) {
            case INFO:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.collect_info)));
                break;
            case DYNAMIC:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string
                        .collect_dynamic)));
                break;
            case QA:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.qa)));
                break;
            case QA_ANSWER:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.qa_answer)));
                break;
            case CIRCLE:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.circle)));
                break;
            case CIRCLE_POST:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.circle_post)));
                break;
            case CIRCLE_COMMENT:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.comment)));
                break;
            case COMMENT:
                mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getUser().getName(), getString(R.string.comment)));
                break;
            case USER:
                mTvTitle.setText(getString(R.string.report_title_format_single, mReportResourceBean.getUser().getName()));

                break;
            default:
        }
        // title 点击
        ConvertUtils.stringLinkConvert(mTvTitle, setLiknks(), true);
        if (!TextUtils.isEmpty(mReportResourceBean.getImg())) {
            Glide.with(this)
                    .load(ImageUtils.imagePathConvertV2(mReportResourceBean.getImg(), AppApplication.getTOKEN()))
                    .placeholder(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_error_image)
                    .centerCrop()
                    .into(mIvImg);

        } else {
            mIvImg.setVisibility(View.GONE);
        }
        // 资源 title
        if (!TextUtils.isEmpty(mReportResourceBean.getTitle())) {
            mTvResourceTitle.setText(mReportResourceBean.getTitle());
        } else {
            mTvResourceTitle.setVisibility(View.GONE);
            mTvResourceDes.setMaxLines(DEFAULT_RESOURCE_DES_MAX_LINES);
        }
        /**
         * 增加文字描述付费模糊
         */
        if (!TextUtils.isEmpty(mReportResourceBean.getDes())) {
            if (mReportResourceBean.isDesCanlook()) {
                mTvResourceDes.setText(mReportResourceBean.getDes());
            } else {
                int startPosition = mReportResourceBean.getDes().length();
                mReportResourceBean.setDes(mReportResourceBean.getDes() + getString(R.string.pay_blur_tip));
                TextViewUtils.newInstance(mTvResourceDes, mReportResourceBean.getDes())
                        .spanTextColor(SkinUtils.getColor(R
                                .color.normal_for_assist_text))
                        .position(startPosition, mReportResourceBean.getDes().length())
                        .maxLines(DEFAULT_RESOURCE_DES_MAX_LINES)
                        .disPlayText(false)
                        .build();
                mTvResourceDes.setVisibility(View.VISIBLE);
            }
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

    /**
     * 更新用户信息回调
     *
     * @param data
     */
    @Override
    public void getUserInfoResult(UserInfoBean data) {
        if (data == null) {
            mReportResourceBean.setUser(DefaultUserInfoConfig.getDefaultDeletUserInfo(mActivity, mReportResourceBean.getUser().getUser_id()));
        } else {
            mReportResourceBean.setUser(data);
        }
        updateData();

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
        if (!TextUtils.isEmpty(mReportResourceBean.getUser().getName())) {
            Link link = new Link(mReportResourceBean.getUser().getName())
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
            case CIRCLE_COMMENT:

                break;
            case COMMENT:

                break;

            case USER:

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

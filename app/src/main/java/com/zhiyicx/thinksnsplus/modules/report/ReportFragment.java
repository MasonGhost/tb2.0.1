package com.zhiyicx.thinksnsplus.modules.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
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

    private static final String BUNDLE_REPORT_RESOURCE_DATA = "report_resource_data";
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

    private ReportResourceBean mReportResourceBean;

    public static ReportFragment newInstance(Bundle bundle) {
        ReportFragment reportFragment = new ReportFragment();
        reportFragment.setArguments(bundle);
        return reportFragment;
    }


    /**
     * @param context            not application context clink
     * @param reportResourceBean report data {@link ReportResourceBean}
     */
    public static void startReportActivity(Context context, ReportResourceBean reportResourceBean) {

        Intent intent = new Intent(context, RewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REPORT_RESOURCE_DATA, reportResourceBean);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
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

        if (!TextUtils.isEmpty(mReportResourceBean.getTitle())) {
            mTvTitle.setText(getString(R.string.report_title_format, mReportResourceBean.getTitle()));
        }
        if (!TextUtils.isEmpty(mReportResourceBean.getDes())) {
            mTvDes.setText(mReportResourceBean.getDes());
        }
        if (!TextUtils.isEmpty(mReportResourceBean.getImg())) {
            ImageUtils.loadImageDefault(mIvImg, mReportResourceBean.getImg());
        }
    }


    private void initListener() {

        mEtReportContent.setBackgroundResource(R.drawable.shape_rect_bg_boder_gray);

        // 内容变化监听
        RxTextView.textChanges(mEtReportContent.getEtContent())
                .map(charSequence -> charSequence.toString().replaceAll(" ", "").length() > 0)
                .subscribe(aBoolean ->
                        mToolbarRight.setEnabled(aBoolean)
                );
        // 举报点击
        RxView.clicks(mBtReport)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mEtReportContent.getEtContent());
                    mPresenter.report(mEtReportContent.getInputContent(),mReportResourceBean);
                });
        // 资源点击
        RxView.clicks(mLlResourceContianer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    goDetail();

                });
        // title 点击
        ConvertUtils.stringLinkConvert(mTvTitle, setLiknks(), true);

    }

    private List<Link> setLiknks() {
        List<Link> links = new ArrayList<>();
        if (!TextUtils.isEmpty(mReportResourceBean.getTitle())) {
            Link commentNameLink = new Link(mReportResourceBean.getTitle())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color
                            .important_for_content))
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                            .themeColor))
                    .setHighlightAlpha(.8f)
                    .setUnderlined(false)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        goDetail();
                    });
            links.add(commentNameLink);
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
            default:
        }
    }

}

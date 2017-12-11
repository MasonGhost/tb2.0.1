package com.zhiyicx.thinksnsplus.modules.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

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

    private static final String BUNDLE_REPORT_RESOURCE_DATA = "report_resource_data";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_resource_contianer)
    LinearLayout mLlResourceContianer;
    @BindView(R.id.et_report_content)
    UserInfoInroduceInputView mEtReportContent;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;

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
    protected void initView(View rootView) {
        initRightSubmit();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_report;
    }

    private void initRightSubmit() {

        mEtReportContent.setBackgroundResource(R.drawable.shape_rect_bg_boder_gray);
        RxTextView.textChanges(mEtReportContent.getEtContent())
                .map(charSequence -> charSequence.toString().replaceAll(" ", "").length() > 0).subscribe(aBoolean ->
                mToolbarRight.setEnabled(aBoolean)
        );

        RxView.clicks(mBtLoginLogin)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mBtLoginLogin);
                });
    }

}

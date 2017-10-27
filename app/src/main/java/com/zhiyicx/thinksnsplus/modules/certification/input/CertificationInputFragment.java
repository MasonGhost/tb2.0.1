package com.zhiyicx.thinksnsplus.modules.certification.input;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.InfoInputEditText;
import com.zhiyicx.baseproject.widget.edittext.SEditText;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.modules.certification.send.SendCertificationActivity;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import org.simple.eventbus.Subscriber;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;


import static android.view.View.VISIBLE;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.send.SendCertificationActivity.BUNDLE_SEND_CERTIFICATION;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class CertificationInputFragment extends TSFragment<CertificationInputContract.Presenter>
        implements CertificationInputContract.View {

    @BindView(R.id.tv_name)
    InfoInputEditText mTvName;
    @BindView(R.id.tv_id_card)
    InfoInputEditText mTvIdCard;
    @BindView(R.id.tv_phone)
    InfoInputEditText mTvPhone;
    @BindView(R.id.ll_company_personage)
    LinearLayout mLlCompanyPersonage;
    @BindView(R.id.tv_company_name)
    InfoInputEditText mTvCompanyName;
    @BindView(R.id.tv_company_address)
    InfoInputEditText mTvCompanyAddress;
    @BindView(R.id.tv_company_principal)
    InfoInputEditText mTvCompanyPrincipal;
    @BindView(R.id.tv_company_principal_id_card)
    InfoInputEditText mTvCompanyPrincipalIdCard;
    @BindView(R.id.tv_company_principal_phone)
    InfoInputEditText mTvCompanyPrincipalPhone;
    @BindView(R.id.ll_company)
    LinearLayout mLlCompany;
    @BindView(R.id.edit_input_description)
    UserInfoInroduceInputView mTvDescription;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.bt_to_send)
    LoadingButton mBtToSend;


    private int mType; // 申请的类型
    private SendCertificationBean mSendBean;
    private String mLimitTipStr = "{}/";// 添加格式符号，用户ColorPhrase
    private int mLimitMaxSize;
    private int mShowLimitMaxSize;

    public CertificationInputFragment instance(Bundle bundle) {
        CertificationInputFragment fragment = new CertificationInputFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        // 适配手机无法显示输入焦点
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        mType = getArguments().getInt(BUNDLE_TYPE);
        if (mType == 0) {
            mLlCompanyPersonage.setVisibility(VISIBLE);
        } else {
            mLlCompany.setVisibility(VISIBLE);
        }
        // 限制身份证输入英文和数字
        String digists = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        mTvIdCard.getEditInput().setKeyListener(DigitsKeyListener.getInstance(digists));
        mTvCompanyPrincipalIdCard.getEditInput().setKeyListener(DigitsKeyListener.getInstance(digists));

        mTvDescription.getEtContent().setGravity(Gravity.RIGHT);
        mTvDescription.getEtContent(). setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

    }

    @Override
    protected void initData() {
        mSendBean = new SendCertificationBean();
        mSendBean.setType(mType == 0 ? SendCertificationBean.USER : SendCertificationBean.ORG);
        mLimitMaxSize = getResources().getInteger(R.integer.certification_description_max_length);
        mShowLimitMaxSize = getResources().getInteger(R.integer.certification_description_show_limit_length);
        initListener();
        mTvName.getEditInput().requestFocus();
    }

    private void initListener() {
        // 个人认证
        RxTextView.textChanges(mTvName.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setName(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvIdCard.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setNumber(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvPhone.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setPhone(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        // 企业认证
        RxTextView.textChanges(mTvCompanyName.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setOrg_name(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvCompanyAddress.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setOrg_address(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvCompanyPrincipal.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setName(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvCompanyPrincipalIdCard.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setNumber(String.valueOf(charSequence));
                    setConfirmEnable();
                });
        RxTextView.textChanges(mTvCompanyPrincipalPhone.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setPhone(String.valueOf(charSequence));
                    setConfirmEnable();
                });

        RxTextView.textChanges(mTvDescription.getEtContent())
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mSendBean.setDesc(String.valueOf(charSequence));
                    setConfirmEnable();
                });


        RxView.clicks(mBtToSend)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (checkData()) {
                        // 数据正常 跳转到下一页 选择图片
                        showErrorTips("");
                        Intent intent = new Intent(getActivity(), SendCertificationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BUNDLE_SEND_CERTIFICATION, mSendBean);
                        intent.putExtra(BUNDLE_SEND_CERTIFICATION, bundle);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_certification_input;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.certification_basic_info);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    /**
     * 检查数据
     */
    private boolean checkData() {
        if (!RegexUtils.isIDCard18(mSendBean.getNumber())) {
            showErrorTips(getString(R.string.alert_error_id_card));
            return false;
        }
        if (!RegexUtils.isMobileExact(mSendBean.getPhone())) {
            showErrorTips(getString(R.string.phone_number_toast_hint));
            return false;
        }
        return true;
    }

    @Override
    public void showErrorTips(String error) {
        if (TextUtils.isEmpty(error)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(VISIBLE);
            mTvErrorTip.setText(error);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void sendSuccess() {
        getActivity().finish();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS)
    public void close(Bundle bundle){
        // 发布成功
        sendSuccess();
    }

    /**
     * 设置按钮是否可点击
     */
    private void setConfirmEnable() {
        if (mType == 0) {
            if (TextUtils.isEmpty(mSendBean.getName())
                    || TextUtils.isEmpty(mSendBean.getNumber())
                    || TextUtils.isEmpty(mSendBean.getPhone())
                    || TextUtils.isEmpty(mSendBean.getDesc())) {
                mBtToSend.setEnabled(false);
            } else {
                mBtToSend.setEnabled(true);
            }
        } else {
            if (TextUtils.isEmpty(mSendBean.getOrg_name())
                    || TextUtils.isEmpty(mSendBean.getOrg_address())
                    || TextUtils.isEmpty(mSendBean.getName())
                    || TextUtils.isEmpty(mSendBean.getNumber())
                    || TextUtils.isEmpty(mSendBean.getPhone())
                    || TextUtils.isEmpty(mSendBean.getDesc())) {
                mBtToSend.setEnabled(false);
            } else {
                mBtToSend.setEnabled(true);
            }
        }
    }
}

package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import com.trycatch.mysnackbar.Prompt
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.config.PayConfig
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow
import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.UIUtils
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailContainerFragment
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment
import java.util.concurrent.TimeUnit

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class IntegrationWithdrawalsFragment : TSFragment<IntegrationWithdrawalsContract.Presenter>(), IntegrationWithdrawalsContract.View {

    @BindView(R.id.tv_ratio)
    lateinit var mTvMineIntegration: TextView
    @BindView(R.id.tv_recharge_rule)
    lateinit var mTvRechargeRule: TextView
    @BindView(R.id.tv_toolbar_center)
    lateinit var mTvToolbarCenter: TextView
    @BindView(R.id.tv_toolbar_left)
    lateinit var mTvToolbarLeft: TextView
    @BindView(R.id.tv_toolbar_right)
    lateinit var mTvToolbarRight: TextView
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar
    @BindView(R.id.et_input)
    lateinit var mEtInput: EditText
    @BindView(R.id.bt_sure)
    lateinit var mBtSure: TextView
    @BindView(R.id.tv_input_tip1)
    lateinit var mTvInputTip1: TextView
    @BindView(R.id.tv_input_tip2)
    lateinit var mTvInputTip2: TextView

    private val mBaseRatioNum = 100

    private var mIntegrationConfigBean: IntegrationConfigBean? = null

    /**
     * 充值提示规则选择弹框
     */
    private var mRechargeInstructionsPopupWindow: ActionPopupWindow? = null// recharge instruction pop


    private var mRechargeMoney = 0.toDouble() // money choosed for recharge

    private var mGoldName: String? = null

    override fun useEventBus() = true

    override fun showToolBarDivider() = false

    override fun setUseSatusbar() = true

    override fun setUseStatusView() = false

    override fun showToolbar() = false

    override fun setStatusbarGrey() = false


    override fun getBodyLayoutId() = R.layout.fragment_integration_withdrawals


    override fun initView(rootView: View) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent)
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh) as ImageView
        mToolbar.setBackgroundResource(android.R.color.transparent)
        (mToolbar.layoutParams as LinearLayout.LayoutParams).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0)
        mGoldName = mPresenter.integrationGoldName
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white))
        mTvToolbarCenter.text = getString(R.string.integration_withdrawals_format, mGoldName)
        mTvToolbarRight.text = getString(R.string.withdrawals_record)
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(context, R.mipmap.topbar_back_white), null, null, null)
    }

    override fun initData() {
        if (arguments != null) {
            mIntegrationConfigBean = arguments.getSerializable(BUNDLE_DATA) as IntegrationConfigBean
        }
        if (mIntegrationConfigBean == null) {
            return
        }
        mEtInput.hint = getString(R.string.et_input_withdrawals_integration_tip_format, mIntegrationConfigBean!!.cashmin, mGoldName)
        initListener()
        // 元对应的积分比例，服务器返回的是以分为单位的比例
        setDynamicRatio(mBaseRatioNum)
        mTvRechargeRule.text = resources.getString(R.string.integration_withdrawals_rule_format, mGoldName)
        mTvInputTip1.text = getString(R.string.input_withdrawals_integration_format, mGoldName)
        mTvInputTip2.text = getString(R.string.input_withdrawals_integration_tip_format, mGoldName)
    }

    /**
     * 动态显示提取金额
     *
     * @param currentIntegration
     */
    private fun setDynamicRatio(currentIntegration: Int) {
        mTvMineIntegration.text = getString(R.string.integration_2_money_ratio_formart, currentIntegration, mGoldName, PayConfig.realCurrencyFen2Yuan((currentIntegration.toFloat() / mIntegrationConfigBean!!.rechargeratio).toDouble()))
    }

    override fun setRightClick() {
        super.setRightClick()
        startActivity(Intent(activity, BillActivity::class.java))
    }

    private fun initListener() {
        // 积分规则
        RxView.clicks(mTvRechargeRule)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe {
                    val intent = Intent(mActivity, WalletRuleActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(WalletRuleFragment.BUNDLE_RULE, mIntegrationConfigBean!!.cashrule)
                    bundle.putString(WalletRuleFragment.BUNDLE_TITLE, resources.getString(R.string
                            .integration_withdrawals_rule_format, mGoldName))

                    intent.putExtras(bundle)
                    startActivity(intent)
                }
        // 充值记录
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe {
                    val intent = Intent(mActivity, IntegrationRWDetailActivity::class.java)
                    intent.putExtra(IntegrationRWDetailContainerFragment.BUNDLE_DEFAULT_POSITION, IntegrationRWDetailContainerFragment
                            .POSITION_WITHDRAWASL_RECORD)
                    startActivity(intent)
                }
        // 返回
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { mActivity.finish() }


        // 确认
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe {
                    when {
                        mRechargeMoney < mIntegrationConfigBean!!.cashmin -> showSnackErrorMessage(getString(R.string.please_more_than_min_withdrawals_formart, mIntegrationConfigBean!!.cashmin))
                        mRechargeMoney > mIntegrationConfigBean!!.cashmax -> showSnackErrorMessage(getString(R.string.please_less_max_withdrawals_formart, mIntegrationConfigBean!!.cashmax))
                        mRechargeMoney > mRechargeMoney.toInt() -> initmRechargeInstructionsPop()
                        else -> {
                            setSureBtEnable(false)
                            // 传入的是真实货币分单位
                            mPresenter.integrationWithdrawals(mRechargeMoney.toInt())
                        }
                    }
                }

        RxTextView.textChanges(mEtInput).subscribe({ charSequence ->
            val mRechargeMoneyStr = charSequence.toString()
            mRechargeMoney = if (mRechargeMoneyStr.replace(" ".toRegex(), "").isNotEmpty()) {
                java.lang.Double.parseDouble(mRechargeMoneyStr)
            } else {
                0.0
            }
            configSureButton()
        }) { throwable ->
            throwable.printStackTrace()
            setCustomMoneyDefault()
            mRechargeMoney = 0.0
            configSureButton()
        }

    }

    override fun snackViewDismissWhenTimeOut(prompt: Prompt) {
        super.snackViewDismissWhenTimeOut(prompt)
        if (activity != null && Prompt.SUCCESS == prompt) {
            activity.finish()
        }
    }

    /**
     * 设置自定义金额数量
     */
    private fun setCustomMoneyDefault() = mEtInput.setText("")

    /**
     * 检查确认按钮是否可点击
     */
    private fun configSureButton() {
        setSureBtEnable(mRechargeMoney > 0)
        setDynamicRatio(mRechargeMoney.toInt())
    }

    override fun setSureBtEnable(enable: Boolean) {
        mBtSure.isEnabled = enable
    }

    /**
     * 充值说明选择弹框
     */
    fun initmRechargeInstructionsPop() {
        DeviceUtils.hideSoftKeyboard(mActivity, mEtInput)
        if (mRechargeInstructionsPopupWindow != null) {
            mRechargeInstructionsPopupWindow!!.show()
            return
        }
        mRechargeInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.recharge_instructions))
                .desStr(getString(R.string.recharge_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(activity)
                .bottomClickListener { mRechargeInstructionsPopupWindow!!.hide() }
                .build()
        mRechargeInstructionsPopupWindow!!.show()
    }


    companion object {


        val BUNDLE_DATA = "data"


        fun newInstance(bundle: Bundle): IntegrationWithdrawalsFragment {
            val integrationRechargeFragment = IntegrationWithdrawalsFragment()
            integrationRechargeFragment.arguments = bundle
            return integrationRechargeFragment
        }
    }

}

package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import com.pingplusplus.android.Pingpp
import com.trycatch.mysnackbar.Prompt
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.config.PayConfig
import com.zhiyicx.baseproject.widget.button.CombinationButton
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow
import com.zhiyicx.common.config.ConstantConfig
import com.zhiyicx.common.utils.ConvertUtils
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.UIUtils
import com.zhiyicx.common.utils.log.LogUtils
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.recharge_withdrawal.IntegrationRWDetailContainerFragment
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment
import com.zhiyicx.thinksnsplus.widget.chooseview.ChooseDataBean
import com.zhiyicx.thinksnsplus.widget.chooseview.SingleChooseView
import com.zhiyicx.tspay.TSPayClient
import org.simple.eventbus.EventBus

import java.util.ArrayList
import java.util.Arrays
import java.util.concurrent.TimeUnit

import butterknife.BindView

import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class IntegrationRechargeFragment : TSFragment<IntegrationRechargeContract.Presenter>(), IntegrationRechargeContract.View, SingleChooseView.OnItemChooseChangeListener {

    @BindView(R.id.tv_recharge_ratio)
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
    @BindView(R.id.choose_view)
    lateinit var mChooseView: SingleChooseView
    @BindView(R.id.ll_recharge_choose_money_item)
    lateinit var mLlRechargeChooseMoneyItem: LinearLayout
    @BindView(R.id.et_input)
    lateinit var mEtInput: EditText
    @BindView(R.id.bt_recharge_style)
    lateinit var mBtRechargeStyle: CombinationButton
    @BindView(R.id.bt_sure)
    lateinit var mBtSure: TextView

    private var mPayType: String? = null     // type for recharge
    private var mIntegrationConfigBean: IntegrationConfigBean? = null
    private var mPayChargeId: String? = null // recharge lables

    /**
     * 充值提示规则选择弹框
     */
    private var mPayStylePopupWindow: ActionPopupWindow? = null// pay type choose pop
    private var mRechargeInstructionsPopupWindow: ActionPopupWindow? = null// recharge instruction pop


    override var money: Double = 0.toDouble() // money choosed for recharge

    private var mGoldName: String? = null

    override fun useEventBus(): Boolean {
        return true
    }

    override fun showToolBarDivider(): Boolean {
        return false
    }

    override fun setUseSatusbar(): Boolean {
        return true
    }

    override fun setUseStatusView(): Boolean {
        return false
    }

    override fun showToolbar(): Boolean {
        return false
    }

    override fun setStatusbarGrey(): Boolean {
        return false
    }


    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_integration_recharge
    }

    override fun initView(rootView: View) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent)
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh) as ImageView
        mToolbar.setBackgroundResource(android.R.color.transparent)
        (mToolbar.layoutParams as LinearLayout.LayoutParams).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0)
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white))
        mGoldName = mPresenter.goldName
        mTvToolbarCenter.text = getString(R.string.recharge_integration_foramt, mGoldName)
        mTvToolbarRight.text = getString(R.string.recharge_record)
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(context, R.mipmap.topbar_back_white), null, null, null)

        initListener()
        mLlRechargeChooseMoneyItem.visibility = View.VISIBLE
    }

    override fun initData() {
        if (arguments != null) {
            mIntegrationConfigBean = arguments.getSerializable(BUNDLE_DATA) as IntegrationConfigBean
        }
        if (mIntegrationConfigBean == null) {
            mPresenter.getIntegrationConfigBean()
            return
        }
        updateData()

    }

    /**
     * 更新配置信息
     *
     * @param isGetSuccess
     * @param data
     */
    override fun updateIntegrationConfig(isGetSuccess: Boolean, data: IntegrationConfigBean) {
        mIntegrationConfigBean = data
        updateData()
    }

    override fun handleLoading(isShow: Boolean) {
        if (isShow) {
            showLeftTopLoading()
        } else {
            hideLeftTopLoading()
        }
    }


    private fun updateData() {
        // 元对应的积分比例，服务器返回的是以分为单位的比例
        mTvMineIntegration.text = getString(R.string.integration_ratio_formart, 1, mIntegrationConfigBean!!.rechargeratio * 100, mGoldName)
        if (mIntegrationConfigBean != null && !TextUtils.isEmpty(mIntegrationConfigBean!!.rechargeoptions)) {
            val datas = ArrayList<ChooseDataBean>()
            val rechargeoptions = mIntegrationConfigBean!!.rechargeoptions.split(ConstantConfig.SPLIT_SMBOL.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            rechargeoptions
                    .filterNot { TextUtils.isEmpty(it) }
                    .forEach {
                        try {
                            val chooseDataBean = ChooseDataBean()
                            chooseDataBean.text = getString(R.string.money_format, PayConfig.realCurrencyFen2Yuan(java.lang.Double.parseDouble(it)))
                            datas.add(chooseDataBean)
                        } catch (e: Exception) {
                        }
                    }
            mChooseView.updateData(datas)
            mChooseView.setOnItemChooseChangeListener(this)
        } else {
            mChooseView.visibility = View.GONE
        }
    }

    override fun setRightClick() {
        super.setRightClick()
        startActivity(Intent(activity, BillActivity::class.java))
    }

    private fun initListener() {
        // 充值协议
        RxView.clicks(mTvRechargeRule)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe {
                    val intent = Intent(mActivity, WalletRuleActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(WalletRuleFragment.BUNDLE_RULE, mIntegrationConfigBean!!.rechargerule)
                    bundle.putString(WalletRuleFragment.BUNDLE_TITLE, resources.getString(R.string.user_reharge_integration_rule))
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
        // 充值记录
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe {
                    val intent = Intent(mActivity, IntegrationRWDetailActivity::class.java)
                    intent.putExtra(IntegrationRWDetailContainerFragment.BUNDLE_DEFAULT_POSITION, IntegrationRWDetailContainerFragment.POSITION_RECHARGE_RECORD)
                    startActivity(intent)
                }
        // 返回
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { mActivity.finish() }


        // 选择充值方式
        RxView.clicks(mBtRechargeStyle)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe {
                    DeviceUtils.hideSoftKeyboard(context, mBtRechargeStyle)
                    initPayStylePop()
                }
        // 确认
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe {
                    if (PayConfig.realCurrencyYuan2Fen(money) < mIntegrationConfigBean!!.rechargemin) {
                        showSnackErrorMessage(getString(R.string.please_more_than_min_recharge_formart, PayConfig.realCurrencyFen2Yuan(
                                mIntegrationConfigBean!!.rechargemin.toDouble())))

                        return@subscribe
                    }
                    if (PayConfig.realCurrencyYuan2Fen(money) > mIntegrationConfigBean!!.rechargemax) {
                        showSnackErrorMessage(getString(R.string.please_less_min_recharge_formart, PayConfig.realCurrencyFen2Yuan(
                                mIntegrationConfigBean!!.rechargemax.toDouble())))
                        return@subscribe
                    }
                    mBtSure.isEnabled = false
                    mPresenter.getPayStr(mPayType!!, PayConfig.realCurrencyYuan2Fen(money))
                }// 传入的是真实货币分单位

        RxTextView.textChanges(mEtInput!!).subscribe({ charSequence ->
            val mRechargeMoneyStr = charSequence.toString()
            if (mRechargeMoneyStr.replace(" ".toRegex(), "").isNotEmpty()) {
                money = java.lang.Double.parseDouble(mRechargeMoneyStr)
                mChooseView.clearChoose()
            } else {
                money = 0.0
            }
            configSureButton()
        }) { throwable ->
            throwable.printStackTrace()
            setCustomMoneyDefault()
            money = 0.0
            configSureButton()
        }

    }

    /**
     * 设置自定义金额数量
     */
    private fun setCustomMoneyDefault() {
        mEtInput.setText("")
    }

    /**
     * 检查确认按钮是否可点击
     */
    private fun configSureButton() {
        mBtSure.isEnabled = money > 0 && !TextUtils.isEmpty(mBtRechargeStyle.rightText)
    }

    /**
     * 充值方式选择弹框
     */
    private fun initPayStylePop() {
        mSystemConfigBean = mPresenter.systemConfigBean

        val rechargeTypes = ArrayList<String>()
        if (mIntegrationConfigBean!!.recharge_type != null) {
            rechargeTypes.addAll(Arrays.asList(*mIntegrationConfigBean!!.recharge_type))
        }

        if (mPayStylePopupWindow != null) {
            mPayStylePopupWindow!!.show()
            return
        }
        mPayStylePopupWindow = ActionPopupWindow.builder()
                .item1Str(if (rechargeTypes.contains(TSPayClient.CHANNEL_ALIPAY))
                    getString(R.string.choose_pay_style_formart, getString(R.string
                            .alipay))
                else
                    "")
                .item2Str(if (rechargeTypes.contains(TSPayClient.CHANNEL_WXPAY) || rechargeTypes.contains(TSPayClient.CHANNEL_WX))
                    getString(R.string
                            .choose_pay_style_formart, getString(R.string
                            .wxpay))
                else
                    "")
                .item3Str(if (mSystemConfigBean.walletTransform != null && mSystemConfigBean.walletTransform.isOpen)
                    getString(R.string
                            .choose_pay_style_formart, getString(R.string.balance))
                else
                    "")
                .item4Str(if (rechargeTypes.size == 0 && mSystemConfigBean.walletTransform == null || !mSystemConfigBean.walletTransform
                        .isOpen)
                    getString(R.string.recharge_disallow)
                else
                    "")
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(activity)
                .item1ClickListener {
                    mPayType = TSPayClient.CHANNEL_ALIPAY
                    mBtRechargeStyle.rightText = getString(R.string.choose_recharge_style_formart, getString(R.string.alipay))
                    mPayStylePopupWindow!!.hide()
                    configSureButton()
                }
                .item2ClickListener {
                    mPayType = TSPayClient.CHANNEL_WX
                    mBtRechargeStyle.rightText = getString(R.string.choose_recharge_style_formart, getString(R.string.wxpay))
                    mPayStylePopupWindow!!.hide()
                    configSureButton()
                }
                .item3ClickListener {
                    mPayType = TSPayClient.CHANNEL_BALANCE
                    mBtRechargeStyle.rightText = getString(R.string.choose_recharge_style_formart, getString(R.string.balance))
                    mPayStylePopupWindow!!.hide()
                    configSureButton()
                }
                .bottomClickListener { mPayStylePopupWindow!!.hide() }
                .build()
        mPayStylePopupWindow!!.show()
    }


    override fun onItemChooseChanged(position: Int, dataBean: ChooseDataBean?) {
        if (position != -1) {
            mEtInput.setText("")
            try {
                if (dataBean != null) {
                    money = java.lang.Double.parseDouble(dataBean.text)
                }
                configSureButton()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                configSureBtn(true)
                val result = data!!.extras!!.getString("pay_result", "")
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                val errorMsg = data.extras!!.getString("error_msg") // 错误信息
                val extraMsg = data.extras!!.getString("extra_msg") // 错误信息
                val id = UIUtils.getResourceByName("pay_" + result, "string", context)
                if (result.contains("success")) {
                    showSnackSuccessMessage(getString(id))
                } else {
                    showSnackErrorMessage(getString(id))
                }
                if (result == "success") {
                    mPresenter.rechargeSuccess(mPayChargeId!!)
                }
            }
        }
    }

    override fun payCredentialsResult(payStrBean: PayStrV2Bean) {
        mPayChargeId = payStrBean.order.id.toString() + ""
        LogUtils.json(ConvertUtils.object2JsonStr<PayStrV2Bean.ChargeBean>(payStrBean.pingpp_order))
        TSPayClient.pay(ConvertUtils.object2JsonStr<PayStrV2Bean.ChargeBean>(payStrBean.pingpp_order), activity)
    }

    override fun configSureBtn(enable: Boolean) {
        mBtSure.isEnabled = enable
    }

    override fun rechargeSuccess(rechargeSuccessBean: RechargeSuccessBean) {
        EventBus.getDefault().post("", EventBusTagConfig.EVENT_INTEGRATION_RECHARGE)
    }

    override fun snackViewDismissWhenTimeOut(prompt: Prompt, message: String) {
        if (activity != null && Prompt.SUCCESS == prompt && getString(R.string.handle_success) == message) {
            activity.finish()
        }
    }

    /**
     * 充值说明选择弹框
     */
    override fun initmRechargeInstructionsPop() {
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

    override fun useInputMonye(): Boolean {
        return !mEtInput.text.toString().isEmpty()
    }

    companion object {
        val BUNDLE_DATA = "data"


        fun newInstance(bundle: Bundle): IntegrationRechargeFragment {
            val integrationRechargeFragment = IntegrationRechargeFragment()
            integrationRechargeFragment.arguments = bundle
            return integrationRechargeFragment
        }
    }
}

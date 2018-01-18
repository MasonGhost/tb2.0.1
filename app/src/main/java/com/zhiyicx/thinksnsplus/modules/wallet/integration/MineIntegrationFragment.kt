package com.zhiyicx.thinksnsplus.modules.wallet.integration

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toolbar

import com.jakewharton.rxbinding.view.RxView
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.widget.button.CombinationButton
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeActivity
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeFragment
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment

import org.simple.eventbus.Subscriber
import org.simple.eventbus.ThreadMode

import java.util.concurrent.TimeUnit

import butterknife.BindView

import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.thinksnsplus.R.string.find
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_WALLET_RECHARGE
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter.TAG_SHOWRULE_POP
import kotlinx.android.synthetic.main.fragment_cricle_location.*
import kotlinx.android.synthetic.main.fragment_mine_integration.*

/**
 * @Describe 我的积分
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class MineIntegrationFragment : TSFragment<MineIntegrationContract.Presenter>(), MineIntegrationContract.View {
    @BindView(R.id.toolbar)
    @JvmField
    var mToolBar: Toolbar? = null
    @BindView(R.id.tv_toolbar_center)
    @JvmField
    var mTvToolbarCenter: TextView? = null
    @BindView(R.id.tv_toolbar_right)
    @JvmField
    var mTvToolbarRight: TextView? = null
    @BindView(R.id.tv_toolbar_left)
    @JvmField
    var mTvToolbarLeft: TextView? = null
    @BindView(R.id.tv_mine_money)
    @JvmField
    var mTvMineMoney: TextView? = null
    @BindView(R.id.bt_recharge)
    @JvmField
    var mBtReCharge: CombinationButton? = null
    @BindView(R.id.bt_withdraw)
    @JvmField
    var mBtWithdraw: CombinationButton? = null
    @BindView(R.id.tv_recharge_and_withdraw_rule)
    @JvmField
    var mTvReChargeAndWithdrawRule: TextView? = null

    /**
     * 充值提示规则选择弹框
     */
    private var mRulePop: CenterInfoPopWindow? = null

    override fun useEventBus(): Boolean {
        return true
    }

    override fun showToolbar(): Boolean {
        return false
    }

    override fun showToolBarDivider(): Boolean {
        return false
    }

    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_mine_integration
    }

    override fun setLeftImg(): Int {
        return R.mipmap.topbar_back_white
    }

    override fun setStatusbarGrey(): Boolean {
        return false
    }

    override fun initView(rootView: View) {
        mTvToolbarCenter!!.setTextColor(ContextCompat.getColor(mActivity,R.color.white))
        mTvToolbarCenter!!.text = getString(R.string.mine_integration)
        mTvToolbarRight!!.text = getString(R.string.detail)
        mTvToolbarLeft!!.compoundDrawables
        mToolBar!!.setBackgroundResource(android.R.color.transparent)

        initListener()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.updateUserInfo()
    }

    override fun initData() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mPresenter.checkIsNeedTipPop()) {
            view!!.post { mPresenter.checkWalletConfig(TAG_SHOWRULE_POP, false) }
        }
    }

    override fun setRightClick() {
        super.setRightClick()
        startActivity(Intent(activity, BillActivity::class.java))
    }

    private fun initListener() {
        // 充值
        RxView.clicks(mBtReCharge!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_RECHARGE, true) }
        // 提现
        RxView.clicks(mBtWithdraw!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_WITHDRAW, true) }
        // 充值提现规则
        RxView.clicks(mTvReChargeAndWithdrawRule!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> mPresenter.checkWalletConfig(WalletPresenter.TAG_SHOWRULE_JUMP, true) }
    }

    private fun jumpWalletRuleActivity() {
        val intent = Intent(activity, WalletRuleActivity::class.java)
        val bundle = Bundle()
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mPresenter.tipPopRule)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /**
     * 初始化登录选择弹框
     */
    private fun showRulePopupWindow() {
        if (mRulePop != null) {
            mRulePop!!.show()
            return
        }
        mRulePop = CenterInfoPopWindow.builder()
                .titleStr(getString(R.string.recharge_and_withdraw_rule))
                .desStr(mPresenter.tipPopRule)
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(activity)
                .buildCenterPopWindowItem1ClickListener { mRulePop!!.hide() }
                .parentView(view)
                .build()
        mRulePop!!.show()
    }

    override fun updateBalance(balance: Double) {
        mTvMineMoney!!.text = getString(R.string.money_format, balance)
    }

    override fun handleLoading(isShow: Boolean) {
        if (isShow) {
            showLeftTopLoading()
        } else {
            hideLeftTopLoading()
        }
    }

    /**
     * the api walletconfig call back
     *
     * @param walletConfigBean wallet config info
     * @param tag              action tag, 1 recharge 2 withdraw
     */
    override fun walletConfigCallBack(walletConfigBean: WalletConfigBean, tag: Int) {
        val bundle = Bundle()
        when (tag) {
            WalletPresenter.TAG_RECHARGE -> {
                bundle.putParcelable(RechargeFragment.BUNDLE_DATA, walletConfigBean)
                jumpActivity(bundle, RechargeActivity::class.java)
            }
            WalletPresenter.TAG_WITHDRAW -> {
                bundle.putParcelable(WithdrawalsFragment.BUNDLE_DATA, walletConfigBean)
                jumpActivity(bundle, WithdrawalsActivity::class.java)
            }
            WalletPresenter.TAG_SHOWRULE_POP -> showRulePopupWindow()
            WalletPresenter.TAG_SHOWRULE_JUMP -> jumpWalletRuleActivity()
        }
    }

    /**
     * activity jump
     *
     * @param bundle intent data
     * @param cls    target class
     */
    private fun jumpActivity(bundle: Bundle, cls: Class<*>) {
        val to = Intent(activity, cls)
        to.putExtras(bundle)
        startActivity(to)
    }

    @Subscriber(tag = EVENT_WALLET_RECHARGE, mode = ThreadMode.MAIN)
    fun onRechargeSuccessUpdate(result: String) {
        initData()
    }

    companion object {

        fun newInstance(): MineIntegrationFragment {
            return MineIntegrationFragment()
        }
    }

}

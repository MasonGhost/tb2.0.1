package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.widget.button.CombinationButton
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow
import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.UIUtils
import com.zhiyicx.common.utils.log.LogUtils
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_INTEGRATION_RECHARGE
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.thinksnsplus.modules.develop.TSDevelopActivity
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter.TAG_SHOWRULE_POP
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeFragment
import com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal.IntegrationWithdrawalsActivity
import com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal.IntegrationWithdrawalsFragment
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsActivity
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithdrawalsFragment
import org.simple.eventbus.Subscriber
import org.simple.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class MineIntegrationFragment : TSFragment<MineIntegrationContract.Presenter>(), MineIntegrationContract.View {

    @BindView(R.id.tv_mine_money)
    lateinit var mTvMineMoney: TextView

    @BindView(R.id.bt_recharge)
    lateinit var mBtReCharge: CombinationButton

    @BindView(R.id.bt_withdraw)
    lateinit var mBtWithdraw: CombinationButton

    @BindView(R.id.bt_mine_integration)
    lateinit var btMineIntegration: CombinationButton

    @BindView(R.id.tv_recharge_and_withdraw_rule)
    lateinit var mTvReChargeAndWithdrawRule: TextView

    @BindView(R.id.tv_toolbar_center)
    lateinit var mTvToolbarCenter: TextView

    @BindView(R.id.tv_toolbar_left)
    lateinit var mTvToolbarLeft: TextView

    @BindView(R.id.tv_toolbar_right)
    lateinit var mTvToolbarRight: TextView

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar


    /**
     * 充值提示规则选择弹框
     */
    private var mRulePop: CenterInfoPopWindow? = null
    private lateinit var mDynamicDetailAdvertHeader: DynamicDetailAdvertHeader

    override fun showToolBarDivider(): Boolean {
        return false
    }

    override fun setUseSatusbar(): Boolean {
        return true
    }

    override fun setStatusbarGrey(): Boolean {
        return false
    }

    override fun setUseStatusView(): Boolean {
        return false
    }

    override fun showToolbar(): Boolean {
        return false
    }

    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_mine_integration
    }

    override fun initView(rootView: View) {
        setStatusPlaceholderViewBackgroundColor(android.R.color.transparent)
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh) as ImageView
        mToolbar.setBackgroundResource(android.R.color.transparent)
        (mToolbar.layoutParams as LinearLayout.LayoutParams).setMargins(0, DeviceUtils.getStatuBarHeight(mActivity), 0, 0)
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white))
        mTvToolbarCenter.text = getString(R.string.mine_integration)
        mTvToolbarRight.text = getString(R.string.detail)
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(context, R.mipmap.topbar_back_white), null, null, null)
        initListener()
        initAdvert(mActivity, mPresenter.integrationAdvert)
        if (mSystemConfigBean.currencyRecharge == null || !mSystemConfigBean.currencyRecharge.isOpen) {
            mBtReCharge.visibility = View.GONE
        } else {
            mBtReCharge.visibility = View.VISIBLE
        }
        if (mSystemConfigBean.currencyCash == null || !mSystemConfigBean.currencyCash.isOpen) {
            mBtWithdraw.visibility = View.GONE
        } else {
            mBtWithdraw.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.updateUserInfo()
    }

    override fun initData() {
    }

    private fun initAdvert(context: Context, adverts: List<RealAdvertListBean>?) {
        mRootView.findViewById(R.id.ll_advert_tag).visibility = View.GONE

        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts == null || adverts.isEmpty()) {
            mRootView.findViewById(R.id.ll_advert).visibility = View.GONE
            return
        }
        mDynamicDetailAdvertHeader = DynamicDetailAdvertHeader(context, mRootView.findViewById(R.id.ll_advert))
        mDynamicDetailAdvertHeader.setAdverts(adverts)
        mDynamicDetailAdvertHeader.setOnItemClickListener { v, position1, url -> toAdvert(context, adverts[position1].advertFormat!!.image.link, adverts[position1].title) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mPresenter.checkIsNeedTipPop()) {
            view!!.post { mPresenter.checkIntegrationConfig(TAG_SHOWRULE_POP, false) }
        }
    }

    private fun initListener() {
        // 明细
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ ->
                    mPresenter.checkIntegrationConfig(MineIntegrationPresenter.TAG_DETAIL, true)
                }
        // 充值积分
        RxView.clicks(mBtReCharge)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ ->
                    mPresenter.checkIntegrationConfig(MineIntegrationPresenter.TAG_RECHARGE, true)
                }
        // 提取积分
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ -> mPresenter.checkIntegrationConfig(MineIntegrationPresenter.TAG_WITHDRAW, true) }     // 提现
        // 积分商城
        RxView.clicks(btMineIntegration)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ -> TSDevelopActivity.startDeveloperAcitvity(mActivity, getString(R.string.integration_shop), R.mipmap.pic_default_mall) }
        // 积分规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ -> mPresenter.checkIntegrationConfig(MineIntegrationPresenter.TAG_SHOWRULE_JUMP, true) }
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { _ -> mActivity.finish() }
    }

    private fun jumpWalletRuleActivity() {
        val intent = Intent(activity, WalletRuleActivity::class.java)
        val bundle = Bundle()
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mPresenter.tipPopRule)
        bundle.putString(WalletRuleFragment.BUNDLE_TITLE, getString(R.string.integration_rule))
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
                .titleStr(getString(R.string.integration_rule))
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

    override fun updateBalance(balance: Long) {
        mTvMineMoney.text = balance.toString()
    }

    override fun handleLoading(isShow: Boolean) {
        if (isShow) {
            showLeftTopLoading()
        } else {
            hideLeftTopLoading()
        }
    }

    /**
     * the api integrationconfig call back
     *
     * @param configBean integration config info
     * @param tag        action tag, 1 recharge 2 withdraw
     */
    override fun integrationConfigCallBack(configBean: IntegrationConfigBean, tag: Int) {
        val bundle = Bundle()
        when (tag) {
            MineIntegrationPresenter.TAG_DETAIL -> {
                bundle.putSerializable(IntegrationDetailListFragment.BUNDLE_INTEGRATION_CONFIG, configBean)
                jumpActivity(bundle, IntegrationDetailActivity::class.java)
            }
            MineIntegrationPresenter.TAG_RECHARGE -> {
                bundle.putSerializable(IntegrationRechargeFragment.BUNDLE_DATA, configBean)
                jumpActivity(bundle, IntegrationRechargeActivity::class.java)
            }
            MineIntegrationPresenter.TAG_WITHDRAW -> {
                bundle.putSerializable(IntegrationWithdrawalsFragment.BUNDLE_DATA, configBean)
                jumpActivity(bundle, IntegrationWithdrawalsActivity::class.java)
            }
            MineIntegrationPresenter.TAG_SHOWRULE_POP -> showRulePopupWindow()
            MineIntegrationPresenter.TAG_SHOWRULE_JUMP -> jumpWalletRuleActivity()
            else -> LogUtils.w(" tag : $tag not support !")
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

    /**
     * jump  to  advert
     *
     * @param context
     * @param link
     * @param title
     */
    private fun toAdvert(context: Context, link: String, title: String) {
        CustomWEBActivity.startToWEBActivity(context, link, title)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscriber(tag = EVENT_INTEGRATION_RECHARGE, mode = ThreadMode.MAIN)
    fun onRechargeSuccessUpdate(result: String) {
        initData()
    }

    companion object {

        fun newInstance(): MineIntegrationFragment {
            return MineIntegrationFragment()
        }
    }

}

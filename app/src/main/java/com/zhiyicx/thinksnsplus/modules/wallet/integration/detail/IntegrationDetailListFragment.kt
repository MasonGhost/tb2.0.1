package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import com.zhiyicx.baseproject.base.TSListFragment
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow
import com.zhiyicx.common.utils.TimeUtils
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import butterknife.BindView

import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:42
 * @Email Jliuer@aliyun.com
 * @Description 账单
 */
class IntegrationDetailListFragment : TSListFragment<IntegrationDetailContract.Presenter, RechargeSuccessV2Bean>(), IntegrationDetailContract.View {

    @BindView(R.id.v_shadow)
    lateinit var mVshadow: View

    @BindView(R.id.tv_rule)
    lateinit var mTvRule: TextView

    private var mActionPopupWindow: ActionPopupWindow? = null
    /**
     * 用于构建 mPresenter
     */
    @Inject
    lateinit var mIntegrationDetailPresenter: IntegrationDetailPresenter


    /**
     * 0 全部，1 收入，-1 支出
     */
    private val mBillTypes = intArrayOf(0, 1, -1)

    private var mBillType = mBillTypes[0]
    /**
     * 明细类型 	筛选类型 recharge - 充值记录 cash - 提现记录 默认为全部
     */
    override var mChooseType: String? = null

    /**
     * 积分配置
     */
    private var mIntegrationConfigBean: IntegrationConfigBean? = null

    private var mGoldName: String? = null


    override val tsAdapter: HeaderAndFooterWrapper<*>
        get() = mHeaderAndFooterWrapper

    override val billType: Int?
        get() = if (mBillType == mBillTypes[0]) null else mBillType

    override fun showToolbar() = mChooseType == null

    override fun showToolBarDivider() = mChooseType == null

    override fun isNeedRefreshDataWhenComeIn() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerIntegrationDetailComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationDetailPresenterModule(IntegrationDetailPresenterModule(this@IntegrationDetailListFragment))
                .build()
                .inject(this@IntegrationDetailListFragment)
        if (arguments != null) {
            mChooseType = arguments.getString(BUNDLE_CHOOSE_TYPE)
            mIntegrationConfigBean = arguments.getSerializable(BUNDLE_INTEGRATION_CONFIG) as IntegrationConfigBean?
        }
        mGoldName = mPresenter.integrationGoldName
    }

    override fun getAdapter(): RecyclerView.Adapter<*> {
        val adapter: CommonAdapter<*>
        // 充值与体现
        if (mChooseType != null) {
            adapter = object : CommonAdapter<RechargeSuccessV2Bean>(activity, R.layout.item_integration_withdrawals_detail,
                    mListDatas) {
                override fun convert(holder: ViewHolder, recharge: RechargeSuccessV2Bean, position: Int) {
                    val desc = holder.getView<TextView>(R.id.withdrawals_desc)
                    val time = holder.getView<TextView>(R.id.withdrawals_time)
                    val account = holder.getView<TextView>(R.id.withdrawals_account)
//                    state 	订单状态 0 - 等待、1 - 完成、-1 - 失败
                    val statusSuccess = recharge.state != -1
                    val action = recharge.type
                    desc.isEnabled = statusSuccess
                    desc.text = if (statusSuccess)
                        if (action < 0) getString(R.string.reduce_format_with_unit, recharge.amount, "") else getString(R.string.increase_format_with_unit, recharge.amount, "")
                    else
                        if (action < 0) getString(R.string.reduce_format_with_unit, 0, "") else  getString(R.string.increase_format_with_unit, 0, "")
                    when (mChooseType) {

                        CHOOSE_TYPE_RECHARGE -> {
                            when (recharge.state) {
                                0 -> account.text = getString(R.string.wait_handle)
                                1 -> account.text = getString(R.string.recharge_success)
                                -1 -> account.text = getString(R.string.recharge_fail)
                                else -> account.text = getDes(recharge)
                            }
                        }
                        CHOOSE_TYPE_CASH -> {
                            when (recharge.state) {
                                0 -> account.text = getString(R.string.rewarding)
                                1 -> account.text = getString(R.string.withdrawals_success)
                                -1 -> account.text = getString(R.string.withdrawals_fail)
                                else -> account.text = getDes(recharge)

                            }
                        }
                        else -> account.text = getDes(recharge)
                    }

                    time.text = TimeUtils.getTimeFriendlyForDetail(recharge.created_at)
                }
            }
        } else {
            // 收入支出，全部
            adapter = object : CommonAdapter<RechargeSuccessV2Bean>(activity, R.layout.item_integration_withdrawals_detail,
                    mListDatas) {
                override fun convert(holder: ViewHolder, recharge: RechargeSuccessV2Bean, position: Int) {
                    val desc = holder.getView<TextView>(R.id.withdrawals_desc)
                    val time = holder.getView<TextView>(R.id.withdrawals_time)
                    val account = holder.getView<TextView>(R.id.withdrawals_account)
                    val statusSuccess = recharge.state == 1
                    val action = recharge.type
                    desc.isEnabled = statusSuccess
                    desc.text = if (statusSuccess)
                        if (action < 0) getString(R.string.reduce_format_with_unit, recharge.amount, "") else getString(R.string.increase_format_with_unit, recharge.amount, "")
                    else
                        getString(if (recharge.state == 0) R.string.bill_doing else R.string.transaction_fail)
                    account.text = getDes(recharge)
                    time.text = TimeUtils.getTimeFriendlyForDetail(recharge.created_at)
                }
            }
            mToolbarCenter.text = getString(R.string.integration_detail_format, mGoldName)
            mTvRule.text = getString(R.string.integration_rule_format, mGoldName)
            mTvRule.visibility = View.VISIBLE
            /**
             * 积分规则
             */
            RxView.clicks(mTvRule)
                    .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                    .compose(bindToLifecycle())
                    .subscribe {
                        val intent = Intent(mActivity, WalletRuleActivity::class.java)
                        if (mIntegrationConfigBean != null) {
                            val bundle = Bundle()
                            bundle.putString(WalletRuleFragment.BUNDLE_RULE, mIntegrationConfigBean!!.rule)
                            bundle.putString(WalletRuleFragment.BUNDLE_TITLE, resources.getString(R.string.integration_rule_format, mGoldName))
                            intent.putExtras(bundle)
                        }
                        startActivity(intent)
                    }

        }
        return adapter
    }

    private fun getDes(recharge: RechargeSuccessV2Bean): String {
        return recharge.title
    }

    override fun getBodyLayoutId() = R.layout.fragment_withdrawals_detail

    override fun setCenterTitle(): String {
        mToolbarCenter.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        //        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.integration_detail)
    }

    override fun setCenterClick() {
        //        mActionPopupWindow.showTop();
    }

    override fun initView(rootView: View) {
        super.initView(rootView)
        //        initTopPopWindow();
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return CustomLinearDecoration(0, resources.getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(context, R.drawable
                .shape_recyclerview_grey_divider))
    }

    private fun initTopPopWindow() {
        if (mActionPopupWindow != null) {
            return
        }
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(activity)
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.withdraw_all))
                .item2Str(getString(R.string.withdraw_out))
                .item3Str(getString(R.string.withdraw_in))
                .item1ClickListener {
                    mToolbarCenter.text = getString(R.string.withdraw_all)
                    mBillType = mBillTypes[0]
                    mActionPopupWindow!!.hide()
                    startRefrsh()
                }
                .item2ClickListener {
                    mToolbarCenter.text = getString(R.string.withdraw_out)
                    mBillType = mBillTypes[2]
                    mActionPopupWindow!!.hide()
                    startRefrsh()

                }
                .item3ClickListener {
                    mToolbarCenter.text = getString(R.string.withdraw_in)
                    mBillType = mBillTypes[1]
                    mActionPopupWindow!!.hide()
                    startRefrsh()
                }
                .dismissListener(object : ActionPopupWindow.ActionPopupWindowShowOrDismissListener {
                    override fun onShow() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowup, 0)
                        mVshadow.visibility = View.VISIBLE
                    }

                    override fun onDismiss() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0)
                        mVshadow.visibility = View.GONE
                    }
                })
                .build()
    }

    companion object {
        const val BUNDLE_INTEGRATION_CONFIG = "config"

        private const val BUNDLE_CHOOSE_TYPE = "CHOOSE_TYPE"
        const val CHOOSE_TYPE_RECHARGE = "recharge"
        const val CHOOSE_TYPE_CASH = "cash"

        /**
         * 构造方法
         *
         * @param configBean
         * @return
         */
        fun newInstance(configBean: IntegrationConfigBean): IntegrationDetailListFragment {
            val integrationDetailListFragment = IntegrationDetailListFragment()
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_INTEGRATION_CONFIG, configBean)
            integrationDetailListFragment.arguments = bundle
            return integrationDetailListFragment
        }

        /**
         * 构造方法
         *
         * @param chooseType
         * @return
         */
        fun newInstance(chooseType: String): IntegrationDetailListFragment {
            val integrationDetailListFragment = IntegrationDetailListFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_CHOOSE_TYPE, chooseType)
            integrationDetailListFragment.arguments = bundle
            return integrationDetailListFragment
        }
    }
}

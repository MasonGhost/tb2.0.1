package com.zhiyicx.thinksnsplus.modules.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxRadioGroup
import com.zhiyicx.appupdate.AppUpdateManager
import com.zhiyicx.appupdate.AppUtils
import com.zhiyicx.appupdate.AppVersionBean
import com.zhiyicx.appupdate.CustomVersionDialogActivity
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.config.ApiConfig
import com.zhiyicx.baseproject.config.ApiConfig.URL_ABOUT_US
import com.zhiyicx.baseproject.widget.button.CombinationButton
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow
import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.SharePreferenceUtils
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackActivity
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity
import com.zhiyicx.thinksnsplus.modules.tb.privacy.PrivacyActivity
import java.util.concurrent.TimeUnit

/**
 * @Describe
 * @author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
class SettingsFragment : TSFragment<SettingsContract.Presenter>(), SettingsContract.View {


    @BindView(R.id.bt_private_manager)
    @JvmField
    var mBtPrivateManager: CombinationButton? = null
    @BindView(R.id.bt_login_out)
    @JvmField
    var mBtLoginOut: CombinationButton? = null
    @BindView(R.id.bt_feedback)
    @JvmField
    var mBtFeedback: CombinationButton? = null
    @BindView(R.id.bt_set_vertify)
    @JvmField
    var mBtSetVertify: CombinationButton? = null
    @BindView(R.id.bt_change_password)
    @JvmField
    var mBtChangePassword: CombinationButton? = null
    @BindView(R.id.bt_clean_cache)
    @JvmField
    var mBtCleanCache: CombinationButton? = null
    @BindView(R.id.bt_about_us)
    @JvmField
    var mBtAboutUs: CombinationButton? = null
    @BindView(R.id.bt_account_manager)
    @JvmField
    var mBtAccountManager: CombinationButton? = null
    @BindView(R.id.bt_check_version)
    @JvmField
    var mBtCheckVersion: CombinationButton? = null
    // 服务器切换使用
    @BindView(R.id.rb_one)
    @JvmField
    var mRbOne: RadioButton? = null
    @BindView(R.id.rb_two)
    @JvmField
    var mRbTwo: RadioButton? = null
    @BindView(R.id.rb_three)
    @JvmField
    var mRbThree: RadioButton? = null
    @BindView(R.id.rb_days_group)
    @JvmField
    var mRbDaysGroup: RadioGroup? = null
    @BindView(R.id.tv_choose_tip)
    @JvmField
    var mTvChooseTip: TextView? = null
    private var mIsDefualtCheck = true

    private var mLoginoutPopupWindow: ActionPopupWindow? = null// 退出登录选择弹框
    private var mCleanCachePopupWindow: ActionPopupWindow? = null// 清理缓存选择弹框

    override fun getBodyLayoutId(): Int {
//        return R.layout.fragment_settings
        return R.layout.fragment_settings_for_tb
    }

    override fun setCenterTitle(): String {
        return getString(R.string.setting)
    }

    override fun setToolBarBackgroud(): Int {
        return R.color.white
    }

    override fun showToolBarDivider(): Boolean {
        return true
    }

    override fun initView(rootView: View) {
        initListener()
        if (com.zhiyicx.common.BuildConfig.USE_DOMAIN_SWITCH) {
            mRbDaysGroup!!.visibility = View.VISIBLE
            mRbOne!!.visibility = View.VISIBLE
            mRbOne!!.text = getString(R.string.domain_formal)
            mRbTwo!!.visibility = View.VISIBLE
            mRbTwo!!.text = getString(R.string.domain_test)
            mRbThree!!.visibility = View.VISIBLE
            mRbThree!!.text = getString(R.string.domain_dev)
            when (ApiConfig.APP_DOMAIN) {
                ApiConfig.APP_DOMAIN_FORMAL -> mRbOne!!.isChecked = true

                ApiConfig.APP_DOMAIN_TEST -> mRbTwo!!.isChecked = true

                ApiConfig.APP_DOMAIN_DEV -> mRbThree!!.isChecked = true
            }
            mTvChooseTip!!.setText(R.string.domain_swith)

            RxRadioGroup.checkedChanges(mRbDaysGroup!!)
                    .subscribe { checkedId ->
                        if (mIsDefualtCheck) {
                            mIsDefualtCheck = false
                        } else {
                            var domain: String? = null
                            when (checkedId) {
                                R.id.rb_one -> domain = ApiConfig.APP_DOMAIN_FORMAL
                                R.id.rb_two -> domain = ApiConfig.APP_DOMAIN_TEST
                                R.id.rb_three -> domain = ApiConfig.APP_DOMAIN_DEV
                            }
                            if (!TextUtils.isEmpty(domain) && mPresenter != null && context != null) {
                                SharePreferenceUtils.saveString(context.applicationContext, SharePreferenceUtils.SP_DOMAIN, domain)
                                mPresenter.loginOut()
                                val mStartActivity = Intent(context, GuideActivity::class.java)
                                val mPendingIntentId = 123456
                                val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                                        PendingIntent
                                                .FLAG_CANCEL_CURRENT)
                                val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
                                System.exit(0)
                            }

                        }
                    }

        } else {
            mRbDaysGroup!!.visibility = View.GONE
            mTvChooseTip!!.visibility = View.GONE
        }

    }

    override fun initData() {
        mPresenter.getDirCacheSize()// 获取缓存大小
        mBtCheckVersion!!.rightText = "V" + DeviceUtils.getVersionName(context)
    }

    override fun setCacheDirSize(size: String) {
        mBtCleanCache!!.rightText = size
    }

    override fun getAppNewVersionSuccess(appVersionBean: List<AppVersionBean>?) {
        if (appVersionBean != null
                && !appVersionBean.isEmpty()
                && AppUtils.getVersionCode(context) < appVersionBean[0].version_code) {
            SharePreferenceUtils.saveObject<Any>(context, CustomVersionDialogActivity.SHAREPREFERENCE_TAG_ABORD_VERION, null)
            AppUpdateManager.getInstance(context, ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_GET_APP_VERSION + "?version_code=" + DeviceUtils.getVersionCode(context) + "&type=android")
                    .startVersionCheck()
        } else {
            showSnackSuccessMessage(getString(R.string.no_new_version))
        }
    }

    private fun initListener() {
        // 认证
        RxView.clicks(mBtSetVertify!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> showSnackSuccessMessage("vertify") }
        // 隐私管理页面
        RxView.clicks(mBtPrivateManager!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid ->
                    // 跳转隐私管理页面
                    val intent = Intent(activity, PrivacyActivity::class.java)
//                    val intent = Intent(activity, InvitationActivity::class.java)
                    startActivity(intent)
                }
        // 修改密码
        RxView.clicks(mBtChangePassword!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> startActivity(Intent(activity, ChangePasswordActivity::class.java)) }
        // 清理缓存
        RxView.clicks(mBtCleanCache!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { aVoid ->
                    initCleanCachePopupWindow()
                    mCleanCachePopupWindow!!.show()
                }
        // 关于我们
        RxView.clicks(mBtAboutUs!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { aVoid -> CustomWEBActivity.startToWEBActivity(context, ApiConfig.APP_DOMAIN + URL_ABOUT_US, getString(R.string.about_us)) }
        // 退出登录
        RxView.clicks(mBtLoginOut!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { aVoid ->
                    initLoginOutPopupWindow()
                    mLoginoutPopupWindow!!.show()
                }
        // 检查版本是否有更新
        RxView.clicks(mBtCheckVersion!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid ->
                    mPresenter.checkUpdate()
                }
        // 意见反馈
        RxView.clicks(mBtFeedback!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe { aVoid ->
                    startActivity(Intent(mActivity, FeedBackActivity::class.java))
                }
    }

    /**
     * 初始化清理缓存选择弹框
     */
    private fun initCleanCachePopupWindow() {
        if (mCleanCachePopupWindow != null) {
            return
        }
        mCleanCachePopupWindow = ActionPopupWindow.builder()
                .item1Str(String.format(getString(R.string.is_sure_clean_cache), mBtCleanCache!!.rightText))
                .item2Str(getString(R.string.determine))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(activity)
                .item2ClickListener {
                    mPresenter.cleanCache()
                    mCleanCachePopupWindow!!.hide()
                }
                .bottomClickListener { mCleanCachePopupWindow!!.hide() }.build()

    }

    /**
     * 初始化登录选择弹框
     */
    private fun initLoginOutPopupWindow() {
        if (mLoginoutPopupWindow != null) {
            return
        }
        mLoginoutPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.is_sure_login_out))
                .item2Str(getString(R.string.login_out_sure))
                .item2Color(ContextCompat.getColor(context, R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(activity)
                .item2ClickListener {
                    if (mPresenter.loginOut()) {
                        startActivity(Intent(activity, LoginActivity::class.java))
                    }
                    mLoginoutPopupWindow!!.hide()
                }
                .bottomClickListener { mLoginoutPopupWindow!!.hide() }.build()


    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }


}

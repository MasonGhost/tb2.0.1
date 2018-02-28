package com.zhiyicx.thinksnsplus.modules.invite.eidtcode

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxRadioGroup
import com.zhiyicx.appupdate.AppUpdateManager
import com.zhiyicx.appupdate.AppUtils
import com.zhiyicx.appupdate.AppVersionBean
import com.zhiyicx.appupdate.CustomVersionDialogActivity
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.config.ApiConfig
import com.zhiyicx.baseproject.utils.WindowUtils
import com.zhiyicx.baseproject.widget.button.CombinationButton
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.SharePreferenceUtils
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity
import com.zhiyicx.thinksnsplus.modules.settings.account.AccountManagementActivity
import com.zhiyicx.thinksnsplus.widget.CheckVersionPopupWindow
import java.util.concurrent.TimeUnit

import butterknife.BindView

import com.zhiyicx.baseproject.config.ApiConfig.URL_ABOUT_US
import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA

/**
 * @Describe 填写邀请码
 * @author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
class EditInviteCodeFragment : TSFragment<EditInviteCodeContract.Presenter>(), EditInviteCodeContract.View {


    @BindView(R.id.bt_login_out)
    lateinit var mBtLoginOut: CombinationButton


    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_settings
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
    }

    override fun initData() {
    }

    private fun initListener() {
        // 认证
        RxView.clicks(mBtSetVertify!!)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { showSnackSuccessMessage("vertify") }
    }

    companion object {

        fun newInstance(): EditInviteCodeFragment {
            return EditInviteCodeFragment()
        }
    }


}

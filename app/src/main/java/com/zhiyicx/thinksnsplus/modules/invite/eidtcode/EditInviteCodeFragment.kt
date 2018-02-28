package com.zhiyicx.thinksnsplus.modules.invite.eidtcode

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME
import com.zhiyicx.thinksnsplus.R
import java.util.concurrent.TimeUnit

/**
 * @Describe 填写邀请码
 * @author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
class EditInviteCodeFragment : TSFragment<EditInviteCodeContract.Presenter>(), EditInviteCodeContract.View {


    @BindView(R.id.et_code)
    lateinit var mEtCode: EditText
    @BindView(R.id.bt_submit)
    lateinit var mBtSubmit: TextView

    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_edit_invite_code
    }

    override fun setCenterTitle(): String {
        return getString(R.string.edit_invite_code)
    }

    override fun setToolBarBackgroud(): Int {
        return R.color.white
    }

    override fun initView(rootView: View) {
        initListener()
    }

    override fun initData() {
    }

    private fun initListener() {
        // 认证
        RxView.clicks(mBtSubmit)
                .throttleFirst(JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { mPresenter.submitInviteCode(mEtCode.text.toString().trim()) }

        RxTextView.afterTextChangeEvents(mEtCode)
                .subscribe { event -> mBtSubmit.isEnabled = !TextUtils.isEmpty(event.editable()) }
    }

    companion object {

        fun newInstance(): EditInviteCodeFragment {
            return EditInviteCodeFragment()
        }
    }


}

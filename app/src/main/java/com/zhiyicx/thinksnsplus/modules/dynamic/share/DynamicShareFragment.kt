package com.zhiyicx.thinksnsplus.modules.dynamic.share

import android.view.View
import android.widget.EditText
import butterknife.BindView
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.thinksnsplus.R

/**
 * @Describe 填写邀请码
 * @author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
class DynamicShareFragment : TSFragment<DynamicShareContract.Presenter>(), DynamicShareContract.View {


    @BindView(R.id.et_code)
    lateinit var mEtCode: EditText


    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_dyanmic_share
    }

    override fun setUseStatusView() = false
    override fun showToolbar() = false
    override fun showToolBarDivider() = false

    override fun initView(rootView: View) {
        initListener()
    }

    override fun initData() {
    }

    private fun initListener() {
    }

    companion object {

        fun newInstance(): DynamicShareFragment {
            return DynamicShareFragment()
        }
    }


}

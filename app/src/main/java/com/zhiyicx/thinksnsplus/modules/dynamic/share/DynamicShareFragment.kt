package com.zhiyicx.thinksnsplus.modules.dynamic.share

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.zhiyicx.baseproject.base.TSFragment
import com.zhiyicx.baseproject.widget.UserAvatarView
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize
import com.zhiyicx.common.config.ConstantConfig
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.utils.ImageUtils
import java.util.concurrent.TimeUnit
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.app.Activity
import android.graphics.Rect
import com.zhiyicx.common.utils.DeviceUtils


/**
 * @Describe 分享快讯
 * @author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
class DynamicShareFragment : TSFragment<DynamicShareContract.Presenter>(), DynamicShareContract.View {


    @BindView(R.id.ll_share_container)
    lateinit var mLlShareContainer: LinearLayout

    @BindView(R.id.iv_qr)
    lateinit var mIvQR: ImageView

    @BindView(R.id.iv_wechat)
    lateinit var mIvWechat: ImageView


    @BindView(R.id.iv_wechat_friends)
    lateinit var mIvWechatFriends: ImageView

    @BindView(R.id.iv_phone)
    lateinit var mIvSave: ImageView

    @BindView(R.id.tv_qr_tip)
    lateinit var mTvQRTip: TextView

    @BindView(R.id.iv_back)
    lateinit var mTvBack: TextView

    /**
     * 内容
     */
    @BindView(R.id.iv_headpic)
    lateinit var mIvHeadePic: UserAvatarView

    @BindView(R.id.tv_name)
    lateinit var mTvName: TextView

    @BindView(R.id.tv_time)
    lateinit var mTvTime: TextView

    @BindView(R.id.tv_content)
    lateinit var mTvContent: SpanTextViewWithEllipsize

    @BindView(R.id.v_line)
    lateinit var mVline: View
    @BindView(R.id.dlmv_menu)
    lateinit var mDlmvMenu: View
    @BindView(R.id.dcv_comment)
    lateinit var mDcvComment: View
    @BindView(R.id.item_contaienr)
    lateinit var mItemContainer: View
    @BindView(R.id.ll_bottom_container)
    lateinit var mLlBottom_container: View

    private var mDynamicShareBean: DynamicShareBean? = null

    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_dyanmic_share
    }

    override fun setUseStatusView() = false
    override fun setUseSatusbar() = true
    override fun showToolbar() = false
    override fun showToolBarDivider() = false

    override fun initView(rootView: View) {
        mDynamicShareBean = arguments.getSerializable(BUNDLE_SHARE_BEAN) as DynamicShareBean?
        mVline.visibility = View.GONE
        mDlmvMenu.visibility = View.GONE
        mDcvComment.visibility = View.GONE
        initListener()
    }

    override fun initData() {
        if (mDynamicShareBean == null) {
            mItemContainer.visibility = View.GONE
            return
        }
        ImageUtils.loadCircleUserHeadPic(mDynamicShareBean!!.userInfoBean, mIvHeadePic)
        mTvName.text = mDynamicShareBean!!.userInfoBean.name
        mTvTime.text = mDynamicShareBean!!.time
        mTvContent.setMaxlines(Int.MAX_VALUE)
        mTvContent.text = mDynamicShareBean!!.content
        mTvQRTip.text = getString(R.string.scan_get_candy, mPresenter.walletGoldName)


    }

    private fun initListener() {
        // 返回
        RxView.clicks(mTvBack)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { activity.finish() }
        // 微信
        RxView.clicks(mIvWechat)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { activity.finish() }
        // 朋友圈
        RxView.clicks(mIvWechatFriends)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe { activity.finish() }
        // 保存
        RxView.clicks(mIvSave)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME.toLong(), TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe {

                    mIvQR.setImageBitmap(takeScreenShot())
                }
    }

    // 截取指定图
    private fun takeScreenShot(): Bitmap {
        // View是你需要截图的View
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache

        // 获取屏幕长和高
        val width = DeviceUtils.getScreenWidth(activity)
        val height = DeviceUtils.getScreenHeight(activity)
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        val b = Bitmap.createBitmap(b1, 0, 0, width, height - mLlBottom_container.height)
        view.destroyDrawingCache()
        return b
    }

    companion object {
        const val BUNDLE_SHARE_BEAN = "share_bean"

        fun newInstance(dynamicShareBean: DynamicShareBean): DynamicShareFragment {
            val dynamicShareFragment = DynamicShareFragment()
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_SHARE_BEAN, dynamicShareBean)
            dynamicShareFragment.arguments = bundle
            return dynamicShareFragment
        }
    }


}

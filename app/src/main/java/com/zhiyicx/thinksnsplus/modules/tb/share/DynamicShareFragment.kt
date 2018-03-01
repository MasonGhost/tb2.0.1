package com.zhiyicx.thinksnsplus.modules.tb.share

import android.os.Bundle
import android.view.View
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
import android.widget.ScrollView
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import com.zhiyicx.baseproject.config.ApiConfig.URL_INVITE_FIRENDS_FORMAT
import com.zhiyicx.baseproject.config.PathConfig
import com.zhiyicx.baseproject.utils.ExcutorUtil
import com.zhiyicx.common.utils.DeviceUtils
import com.zhiyicx.common.utils.DrawableProvider
import com.zhiyicx.common.utils.FileUtils
import com.zhiyicx.common.utils.TimeUtils
import com.zhiyicx.thinksnsplus.base.AppApplication
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*


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
    lateinit var mTvContent: TextView

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
    @BindView(R.id.sl_container)
    lateinit var mScrollView: ScrollView

    private var mDynamicShareBean: DynamicShareBean? = null

    override fun getBodyLayoutId(): Int {
        return R.layout.fragment_dyanmic_share
    }

    override fun setUseStatusView() = false
    override fun setUseSatusbar() = true
    override fun showToolbar() = false
    override fun showToolBarDivider() = false

    lateinit var mSavingTSnackbar: TSnackbar
    lateinit var mSaveImageSubscription: Subscription

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
        mTvContent.text = mDynamicShareBean!!.content
        mTvQRTip.text = getString(R.string.scan_get_candy, mPresenter.walletGoldName)

        mIvQR.post({ mIvQR.setImageBitmap(ImageUtils.create2Code(getInviteLink(), mIvQR.getHeight())) })
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
                    getSaveBitmapResultObservable(ImageUtils.getBitmapByView2(mScrollView), TimeUtils.getYeayMonthDay(System.currentTimeMillis()))

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

    private fun getSaveBitmapResultObservable(bitmap: Bitmap, name: String) {
        mSaveImageSubscription = Observable.just(1)
                .subscribeOn(Schedulers.io())
                // .subscribeOn(Schedulers.io())  Animators may only be run on Looper threads
                .doOnSubscribe {
                    mSavingTSnackbar = TSnackbar.make(mSnackRootView, getString(R.string.save_pic_ing), TSnackbar.LENGTH_INDEFINITE)
                            .setPromptThemBackground(Prompt.SUCCESS)
                            .addIconProgressLoading(0, true, false)
                            .setMinHeight(0, resources.getDimensionPixelSize(R.dimen.toolbar_height))
                    mSavingTSnackbar.show()
                }
                .map { integer ->
                    val imgName = name + ".jpg"
                    val imgPath = PathConfig.PHOTO_SAVA_PATH
                    DrawableProvider.saveBitmap(bitmap, imgName, imgPath)
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    var data: String = result
                    when (result) {
                        "-1" -> data = getString(R.string.save_failure1)
                        "-2" -> data = getString(R.string.save_failure2)
                        else -> {
                            val file = File(data)
                            if (file.exists()) {
                                data = getString(R.string.save_success) + data
                                FileUtils.insertPhotoToAlbumAndRefresh(mActivity, file)
                            }
                        }
                    }
                    if (mSavingTSnackbar != null) {
                        mSavingTSnackbar.dismiss()
                    }
                    showSnackSuccessMessage(data)
                }
    }

    override fun onDestroy() {
        if (mSaveImageSubscription != null && mSaveImageSubscription.isUnsubscribed()) {
            mSaveImageSubscription.unsubscribe()
        }
        super.onDestroy()
    }

    fun getInviteLink(): String {
        return String.format(Locale.getDefault(), URL_INVITE_FIRENDS_FORMAT, AppApplication.getMyUserIdWithdefault())
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

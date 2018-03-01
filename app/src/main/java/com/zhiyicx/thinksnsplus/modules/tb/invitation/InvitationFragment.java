package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InvitationFragment extends TSFragment<InvitationContract.Presenter> implements InvitationContract.View {

    @BindView(R.id.iv_top_logo)
    ImageView mIvTopLogo;
    @BindView(R.id.tv_invitation_code)
    TextView mTvInvitationCode;
    @BindView(R.id.tv_click_copy)
    TextView mTvClickCopy;
    @BindView(R.id.iv_2code)
    ImageView mIv2code;

    /**
     * 用于分享截图的布局
     */
    @BindView(R.id.ll_share_contianer)
    LinearLayout mLlShareContianer;

    @BindView(R.id.ll_thirdpart_container)
    LinearLayout mLlThirdPartContianer;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_save)
    TextView mTvSave;

    private TSnackbar mSavingTSnackbar;
    private Subscription mSaveImageSubscription;

    public static InvitationFragment newInstance(Bundle bundle) {
        InvitationFragment invitationFragment = new InvitationFragment();
        invitationFragment.setArguments(bundle);
        return invitationFragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        // 设置 二维码
        mIv2code.setImageBitmap(create2Code("ssss", mIv2code.getHeight()));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_invitation;
    }

    @OnClick({R.id.tv_invitation_code, R.id.tv_click_copy, R.id.tv_wx, R.id.tv_friend, R.id.tv_copy, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 邀请码 ， 点击复制
            case R.id.tv_invitation_code:

            case R.id.tv_click_copy:
                ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText(getString(R.string.tb_login_name), mTvInvitationCode.getText());
                if (cm != null) {
                    cm.setPrimaryClip(mClipData);
                }
                ToastUtils.showToast("复制成功，可以发给朋友们了。");
                break;
            // 微信
            case R.id.tv_wx:
                break;
            // 朋友圈
            case R.id.tv_friend:
                break;
            // 复制链接
            case R.id.tv_copy:
                break;
            // 保存
            case R.id.tv_save:
                Bitmap save = takeScreenShot(mLlShareContianer);
                getSaveBitmapResultObservable(save, TimeUtils.getYeayMonthDay(System.currentTimeMillis()));
                break;
            default:
        }
    }

    // 截取指定图
    private Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int width = DeviceUtils.getScreenWidth(mActivity);
        int height = DeviceUtils.getScreenHeight(mActivity);
        Bitmap result = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, width, height - mLlThirdPartContianer.getHeight());
        view.destroyDrawingCache();
        return result;
    }

    private Bitmap create2Code(String str, int size) {
        Bitmap result = QRCodeEncoder.syncEncodeQRCode(str, size);
        return result;
    }

    private void getSaveBitmapResultObservable(final Bitmap bitmap, final String name) {
        mSaveImageSubscription = Observable.just(1)
                .subscribeOn(Schedulers.io())
                // .subscribeOn(Schedulers.io())  Animators may only be run on Looper threads
                .doOnSubscribe(() -> {
                    mSavingTSnackbar = TSnackbar.make(mSnackRootView, getString(R.string.save_pic_ing), TSnackbar.LENGTH_INDEFINITE)
                            .setPromptThemBackground(Prompt.SUCCESS)
                            .addIconProgressLoading(0, true, false)
                            .setMinHeight(0, getResources().getDimensionPixelSize(R.dimen.toolbar_height));
                    mSavingTSnackbar.show();
                })
                .map(integer -> {
                    String imgName = name + ".jpg";
                    String imgPath = PathConfig.PHOTO_SAVA_PATH;
                    return DrawableProvider.saveBitmap(bitmap, imgName, imgPath);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    switch (result) {
                        case "-1":
                            result = getString(R.string.save_failure1);
                            break;
                        case "-2":
                            result = getString(R.string.save_failure2);
                            break;
                        default:
                            File file = new File(result);
                            if (file.exists()) {
                                result = getString(R.string.save_success) + result;
                                FileUtils.insertPhotoToAlbumAndRefresh(mActivity, file);
                            }
                    }
                    if (mSavingTSnackbar != null) {
                        mSavingTSnackbar.dismiss();
                    }
                    showSnackSuccessMessage(result);
                });
    }

    @Override
    public void onDestroy() {
        if (mSaveImageSubscription != null && mSaveImageSubscription.isUnsubscribed()) {
            mSaveImageSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}

package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.baseproject.utils.ExcutorUtil;
import com.zhiyicx.baseproject.widget.popwindow.CenterAlertPopWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.popwindow.TBCenterInfoPopWindow;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.URL_INVITE_FIRENDS_FORMAT;

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
    @BindView(R.id.tv_get_tb_coin)
    TextView mTvGetTbCoin;
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
    protected String setCenterTitle() {
        return getString(R.string.invataion_friends);
    }

    @Override
    protected void initView(View rootView) {
        mTvGetTbCoin.setText(getString(R.string.scan_get_candy, mPresenter.getWalletGoldName()));
    }

    @Override
    protected void initData() {
        // 设置 二维码
        mIv2code.post(() -> mIv2code.setImageBitmap(ImageUtils.create2Code(getInviteLink(), mIv2code.getHeight())));
        mTvInvitationCode.setText(String.valueOf(AppApplication.getMyUserIdWithdefault()));
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
                if (copyStr2Clipboard(mTvInvitationCode.getText().toString())) {
                    ToastUtils.showToast("复制成功，可以发给朋友们了。");
                }
                break;
            // 微信
            case R.id.tv_wx:
                break;
            // 朋友圈
            case R.id.tv_friend:
                break;
            // 复制链接
            case R.id.tv_copy:
                if (copyStr2Clipboard(getInviteLink())) {
                    ToastUtils.showToast("复制成功，可以发给朋友们了。");
                }
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


    private boolean copyStr2Clipboard(String str) {
        ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText(getString(R.string.tb_login_name), str);
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            return true;
        }
        return false;
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

    public String getInviteLink() {
        return String.format(Locale.getDefault(), URL_INVITE_FIRENDS_FORMAT, AppApplication.getMyUserIdWithdefault());
    }
}

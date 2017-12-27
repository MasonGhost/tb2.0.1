package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tbruyelle.rxpermissions.Permission;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @author Catherine
 * @describe 我的二维码页面
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class MyCodeFragment extends TSFragment<MyCodeContract.Presenter> implements MyCodeContract.View {

    @BindView(R.id.iv_user_code)
    AppCompatImageView mIvUserCode;
    @BindView(R.id.user_avatar)
    UserAvatarView mUserAvatar;
    @BindView(R.id.tv_user_name)
    AppCompatTextView mTvUserName;
    @BindView(R.id.tv_user_intro)
    AppCompatTextView mTvUserIntro;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;

    private ActionPopupWindow mScanCodePopupWindow;
    private Bitmap mShareBitmap;

    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
        initScanCodePopupWindow();
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected void initData() {
        mPresenter.getUserInfo();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.my_qr_code_title);
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        // 弹起弹框 选择分享或者扫码
        mScanCodePopupWindow.show();
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_scan;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_code;
    }

    @Override
    public void setMyCode(Bitmap codePic) {
        mIvUserCode.setImageBitmap(codePic);
    }

    @Override
    public void setUserInfo(UserInfoBean userInfo) {
        if (userInfo != null) {
            mTvUserName.setText(userInfo.getName());
            mTvUserIntro.setText(String.format(getString(R.string.default_intro_format), userInfo.getIntro()));
            ImageUtils.loadCircleUserHeadPic(userInfo, mUserAvatar);
            mShareBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), mUserAvatar.getIvAvatar().getDrawable(), R.mipmap.icon);
            Glide.with(getContext())
                    .load(userInfo.getAvatar())
                    .asBitmap()
                    .error(ImageUtils.getDefaultAvatar(userInfo))
                    .placeholder(ImageUtils.getDefaultAvatar(userInfo))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource != null) {
                                mPresenter.createUserCodePic(resource);
                            }
                        }
                    });
        }
    }

    @Override
    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    /**
     * 初始化扫码更多的弹框
     */
    private void initScanCodePopupWindow() {
        mScanCodePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.my_qr_code_title))
                .item2Str(getString(R.string.dynamic_list_share_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 扫一扫
                    // 添加相机权限设置
                    mRxPermissions
                            .requestEach(Manifest.permission.CAMERA)
                            .subscribe(permission -> {
                                if (permission.granted) {
                                    // 权限被允许
                                    startActivity(new Intent(getContext(), ScanCodeActivity.class));
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // 权限没有被彻底禁止
                                } else {
                                    // 权限被彻底禁止
                                    showSnackWarningMessage(getString(R.string.camera_permission_tip));
                                }
                            });


                })
                .item2ClickListener(() -> {
                    // 分享
                    mPresenter.shareMyQrCode(mShareBitmap);
                    mScanCodePopupWindow.hide();
                })
                .bottomClickListener(() -> mScanCodePopupWindow.hide())
                .build();
    }

}

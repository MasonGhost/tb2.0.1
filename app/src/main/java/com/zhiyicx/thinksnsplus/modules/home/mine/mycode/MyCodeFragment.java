package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
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
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getContext(), ScanCodeActivity.class));
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
        if (userInfo != null){
            mTvUserName.setText(userInfo.getName());
            mTvUserIntro.setText(userInfo.getIntro());
            ImageUtils.loadCircleUserHeadPic(userInfo, mUserAvatar);
            Glide.with(getContext())
                    .load(userInfo.getAvatar())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mPresenter.createUserCodePic(resource);
                        }
                    });
        }
    }

    @Override
    public EmptyView getEmptyView() {
        return mEmptyView;
    }

}

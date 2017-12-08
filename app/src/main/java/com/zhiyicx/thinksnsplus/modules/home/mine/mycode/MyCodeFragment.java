package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

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

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        mPresenter.createUserCodePic();
    }

    @Override
    protected String setRightTitle() {
        return "扫一扫";
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getContext(), ScanCodeActivity.class));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_code;
    }

    @Override
    public void setMyCode(Bitmap codePic) {
        mIvUserCode.setImageBitmap(codePic);
    }
}

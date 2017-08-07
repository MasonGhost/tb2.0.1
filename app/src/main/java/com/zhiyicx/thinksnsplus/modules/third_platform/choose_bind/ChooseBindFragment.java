package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.third_platform.bind.BindOldAccountActivity;
import com.zhiyicx.thinksnsplus.modules.third_platform.complete.CompleteAccountActivity;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow.OnItemChooseListener;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindFragment extends TSFragment<ChooseBindContract.Presenter>
        implements ChooseBindContract.View, OnItemChooseListener{

    private ChooseBindPopupWindow mPopupWindow;

    public ChooseBindFragment instance(Bundle bundle){
        ChooseBindFragment fragment = new ChooseBindFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        rootView.postDelayed(this::initPopWindow, 500);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_choose_bind;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.third_platform_bind_account);
    }

    private void initPopWindow(){
        if (mPopupWindow == null){
            mPopupWindow = ChooseBindPopupWindow.Builder()
                    .with(getActivity())
                    .alpha(0.8f)
                    .itemListener(this)
                    .build();
        }
        mPopupWindow.show();
    }

    @Override
    public void onItemChose(int position) {
        if (position == 0){
            // 跳转完善资料
            Intent intent = new Intent(getActivity(), CompleteAccountActivity.class);
            startActivity(intent);
        } else if (position == 1){
            // 跳转绑定已有的账号
            Intent intent = new Intent(getActivity(), BindOldAccountActivity.class);
            startActivity(intent);
        }
    }
}

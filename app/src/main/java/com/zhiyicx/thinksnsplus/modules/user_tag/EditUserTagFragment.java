package com.zhiyicx.thinksnsplus.modules.user_tag;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class EditUserTagFragment extends TSFragment<EditUserTagContract.Presenter> implements EditUserTagContract.View {

    public static EditUserTagFragment newInstance() {
        return new EditUserTagFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
    }


    private void initListener() {
//
//        // 退出登录
//        RxView.clicks(mBtLoginOut)
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .compose(this.<Void>bindToLifecycle())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        initLoginOutPopupWindow();
//                        mLoginoutPopupWindow.show();
//                    }
//                });
    }

}

package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneContainerFragment extends TSFragment {


    public static FindSomeOneContainerFragment newInstance(Bundle bundle) {
        FindSomeOneContainerFragment findSomeOneContainerFragment = new FindSomeOneContainerFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find_some_container;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
        ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager()
                , FindSomeOneContainerViewPagerFragment.initFragment(getActivity().getIntent().getExtras())
                , R.id.fragment_container);


    }

    private void initListener() {
        // 退出登录
//        RxView.clicks(mBtLoginOut)
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .compose(this.bindToLifecycle())
//                .subscribe(aVoid -> {
//                    initLoginOutPopupWindow();
//                    mLoginoutPopupWindow.show();
//                });
    }

}

package com.zhiyicx.thinksnsplus.modules.home.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Describe 主页MainFragment
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MainFragment extends TSViewPagerFragment implements InputLimitView.OnSendClickListener, DynamicFragment.OnCommentClickListener {

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;

    List<Fragment> fragments = new ArrayList<>();

    public void setOnImageClickListener(DynamicFragment.OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    DynamicFragment.OnCommentClickListener mOnCommentClickListener;

    public static MainFragment newInstance(DynamicFragment.OnCommentClickListener l) {
        MainFragment fragment = new MainFragment();
        fragment.setOnImageClickListener(l);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return com.zhiyicx.baseproject.R.layout.fragment_main_viewpager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mVShadow.setAlpha((1 - POPUPWINDOW_ALPHA));
        mVShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIlvComment.getVisibility() == View.VISIBLE) {
                    mIlvComment.setVisibility(View.GONE);
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                }
                v.setVisibility(View.GONE);
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onButtonMenuShow(true);
                }
            }
        });
        mTsvToolbar.setLeftImg(0);//不需要返回键
        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {

        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS, this));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS, this));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this));
        return fragments;
    }

    @Override
    public void onSendClick(View v, String text) {
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        ((DynamicContract.View) fragments.get(mVpFragment.getCurrentItem())).onSendClick(v, text);
    }

    @Override
    public void onButtonMenuShow(boolean isShow) {
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(isShow);
        }
        if (isShow) {
            mVShadow.setVisibility(View.GONE);
            mIlvComment.setVisibility(View.GONE);
            mIlvComment.clearFocus();
            mIlvComment.setSendButtonVisiable(false);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        } else {
            mVShadow.setVisibility(View.VISIBLE);
            mIlvComment.setVisibility(View.VISIBLE);
            mIlvComment.getFocus();
            mIlvComment.setSendButtonVisiable(true);
            DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }

    }

    @Override
    public void setCommentHint(String hintStr) {
        mIlvComment.setEtContentHint(hintStr);
    }
}

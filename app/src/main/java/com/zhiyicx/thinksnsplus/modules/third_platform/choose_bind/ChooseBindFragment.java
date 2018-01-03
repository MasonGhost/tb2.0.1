package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.modules.third_platform.bind.BindOldAccountActivity;
import com.zhiyicx.thinksnsplus.modules.third_platform.complete.CompleteAccountActivity;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow.OnItemChooseListener;

import java.util.Map;

import static com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity.BUNDLE_THIRD_INFO;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindFragment extends TSFragment<ChooseBindContract.Presenter>
        implements ChooseBindContract.View, OnItemChooseListener {

    private ChooseBindPopupWindow mPopupWindow;

    public ChooseBindFragment instance(Bundle bundle) {
        ChooseBindFragment fragment = new ChooseBindFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        rootView.postDelayed(this::initPopWindow, 500);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_choose_bind;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.third_platform_bind_account);
    }

    private void initPopWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = ChooseBindPopupWindow.Builder()
                    .itemlStr(mActivity.getString(R.string.third_platform_complete_register))
                    .item2Str(mActivity.getString(R.string.third_platform_bind_old_account))
                    .with(getActivity())
                    .alpha(0.8f)
                    .itemListener(this)
                    .build();

            boolean openThirdRegister = mSystemConfigBean.getRegisterSettings() == null
                    || mSystemConfigBean.getRegisterSettings().getType() == null
                    || SystemConfig.REGITER_MODE_THIRDPART.equals(mSystemConfigBean.getRegisterSettings().getType())
                    || SystemConfig.REGITER_MODE_ALL.equals(mSystemConfigBean.getRegisterSettings().getType());
            mPopupWindow.canNotRegiterByThirdPlatform(openThirdRegister);
        }
        mPopupWindow.show();
    }

    @Override
    public void onItemChose(int position) {
        if (position == 0) {
            // 跳转完善资料
            Intent intent = new Intent(getActivity(), CompleteAccountActivity.class);
            intent.putExtras(getArguments());
            startActivity(intent);
        } else if (position == 1) {
            // 跳转绑定已有的账号
            Intent intent = new Intent(getActivity(), BindOldAccountActivity.class);
            intent.putExtras(getArguments());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        UMShareAPI mShareAPI = UMShareAPI.get(getContext());
        SHARE_MEDIA share_media;
        switch (((ThridInfoBean) getArguments().getParcelable(BUNDLE_THIRD_INFO)).getProvider()) {
            case ApiConfig.PROVIDER_QQ:
                share_media = SHARE_MEDIA.QQ;
                break;
            case ApiConfig.PROVIDER_WEIBO:
                share_media = SHARE_MEDIA.SINA;

                break;
            case ApiConfig.PROVIDER_WECHAT:
                share_media = SHARE_MEDIA.WEIXIN;

                break;
            default:
                share_media = SHARE_MEDIA.QQ;

        }
        try {
            mShareAPI.deleteOauth(getActivity(), share_media, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}

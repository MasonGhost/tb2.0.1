package com.zhiyicx.thinksnsplus.modules.information.publish;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoFragment extends TSFragment<PublishInfoContract.Presenter> implements PublishInfoContract.View {

    public static PublishInfoFragment getInstance(Bundle bundle) {
        PublishInfoFragment publishInfoFragment = new PublishInfoFragment();
        publishInfoFragment.setArguments(bundle);
        return publishInfoFragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_info;
    }
}

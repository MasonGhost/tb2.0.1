package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishContentFragment extends TSFragment<PublishContentConstact.Presenter> implements
        PublishContentConstact.View{

    public static PublishContentFragment newInstance(Bundle bundle){
        PublishContentFragment publishContentFragment=new PublishContentFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return 0;
    }
}

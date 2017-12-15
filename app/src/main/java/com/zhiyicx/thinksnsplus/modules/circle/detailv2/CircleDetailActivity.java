package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Jliuer
 * @Date 17/11/22 14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailActivity extends TSActivity<CircleDetailPresenter, CircleDetailFragment> {

    @Override
    protected CircleDetailFragment getFragment() {
        return CircleDetailFragment.newInstance(getIntent().getLongExtra(CircleDetailFragment.CIRCLE_ID, 1));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void componentInject() {

    }
}

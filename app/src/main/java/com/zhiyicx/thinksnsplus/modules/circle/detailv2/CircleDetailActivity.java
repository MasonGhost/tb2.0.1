package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;

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
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void componentInject() {

    }

    public static void startCircleDetailActivity(Context context, Long circleId) {
        Intent intent = new Intent(context, CircleDetailActivity.class);
        intent.putExtra(CircleDetailFragment.CIRCLE_ID, circleId);
        context.startActivity(intent);
    }
}

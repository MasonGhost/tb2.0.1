package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.BaseDigItem;

/**
 * @author Jliuer
 * @Date 17/12/11 16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DigListActivity extends TSActivity<DigListPresenter, DigListFragment> {

    @Override
    protected DigListFragment getFragment() {
        return new DigListFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerDigListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .digListPresenterModule(new DigListPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startDigActivity(Context context, long sourceId, BaseDigItem.DigTypeEnum digTypeEnum) {
        Intent intent = new Intent(context, DigListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(DigListFragment.SOURCEID, sourceId);
        bundle.putString(DigListFragment.TYPE, digTypeEnum.value);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2017/8/10 11:30
 * @Email Jliuer@aliyun.com
 * @Description 置顶统一处理
 * <p>
 * Bundle bundle=new Bundle();
 * bundle.putString(StickTopFragment.TYPE,StickTopFragment.TYPE_DYNAMIC);
 * bundle.putLong(StickTopFragment.PARENT_ID,1L);
 * Intent intent=new Intent(context,StickTopActivity.class);
 * startActivity(intent);
 */
public class StickTopActivity extends TSActivity<StickTopPresenter, StickTopFragment> {

    @Override
    protected StickTopFragment getFragment() {
        return StickTopFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerStickTopComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .stickTopPresenterModule(new StickTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}

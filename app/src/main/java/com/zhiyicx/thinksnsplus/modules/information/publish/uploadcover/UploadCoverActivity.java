package com.zhiyicx.thinksnsplus.modules.information.publish.uploadcover;


import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.information.publish.DaggerPublishInfoComponent;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoPresenter;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoPresenterModule;

/**
 * @Describe 完善资讯信息
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
public class UploadCoverActivity extends TSActivity<PublishInfoPresenter, UploadCoverFragment> {

    @Override
    protected UploadCoverFragment getFragment() {
        return UploadCoverFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerPublishInfoComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .publishInfoPresenterModule(new PublishInfoPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}

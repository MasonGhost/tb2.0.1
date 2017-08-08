package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;


import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.information.publish.DaggerPublishInfoComponent;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoFragment;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoPresenter;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoPresenterModule;
/**
 * @Describe  完善资讯信息
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
public class AddInfoActivity extends TSActivity<AddInfoPresenter, AddInfoFragment> {

    @Override
    protected AddInfoFragment getFragment() {
        return AddInfoFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerAddInfoComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .addInfoPresenterModule(new AddInfoPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}

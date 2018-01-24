package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;

/**
 * @Author Jliuer
 * @Date 2018/01/18/9:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeInfoDetailActivity extends BaseMarkdownActivity<EditeInfoDetailPresenter, EditeInfoDetailFragment> {

    @Override
    protected EditeInfoDetailFragment getYourFragment() {
        return EditeInfoDetailFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEditeInfoDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editeInfoDetailPresenterModule(new EditeInfoDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}

package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.DaggerMarkdownComponent;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenterModule;

/**
 * @Author Jliuer
 * @Date 2018/01/18/9:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeInfoDetailActivity extends BaseMarkdownActivity<EditeInfoDetailFragment> {

    @Override
    protected EditeInfoDetailFragment getYourFragment() {
        return EditeInfoDetailFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerMarkdownComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .markdownPresenterModule(new MarkdownPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}

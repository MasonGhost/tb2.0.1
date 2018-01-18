package com.zhiyicx.thinksnsplus.modules.information.publish.news;

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
public class PublishInfoActivityV2 extends BaseMarkdownActivity<PublishInfoFragmentV2> {

    @Override
    protected PublishInfoFragmentV2 getYourFragment() {
        return PublishInfoFragmentV2.getInstance(getIntent().getExtras());
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

package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenter;

/**
 * @Author Jliuer
 * @Date 2018/01/18/9:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeInfoDetailActivity extends BaseMarkdownActivity<MarkdownPresenter,EditeInfoDetailFragment> {

    @Override
    protected EditeInfoDetailFragment getYourFragment() {
        return EditeInfoDetailFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }
}

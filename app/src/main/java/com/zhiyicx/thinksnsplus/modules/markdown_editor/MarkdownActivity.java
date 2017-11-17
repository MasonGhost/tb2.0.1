package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class MarkdownActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return MarkdownFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}

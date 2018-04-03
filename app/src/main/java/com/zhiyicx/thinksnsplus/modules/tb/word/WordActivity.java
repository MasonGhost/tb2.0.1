package com.zhiyicx.thinksnsplus.modules.tb.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.WordResourceBean;
import com.zhiyicx.thinksnsplus.modules.report.ReportPresenterModule;

import static com.zhiyicx.thinksnsplus.modules.tb.word.WordFragment.BUNDLE_WORD_RESOURCE_DATA;


public class WordActivity extends TSActivity<WordPresenter, WordFragment> {
    @Override
    protected WordFragment getFragment() {
        return WordFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerWordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .wordPresenterModule(new WordPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    /**
     * @param context            not application context clink
     * @param wordResourceBean word data {@link WordResourceBean}
     */
    public static void startWordActivity(Context context, WordResourceBean wordResourceBean) {

        Intent intent = new Intent(context, WordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_WORD_RESOURCE_DATA, wordResourceBean);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }
}

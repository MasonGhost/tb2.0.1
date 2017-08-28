package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyPublishQuestionFragment extends TSListFragment<MyPublishQuestionContract.Presenter, QAListInfoBean>
        implements MyPublishQuestionContract.View {

    @Inject
    MyPublishQuestionPresenter mPublishQuestionPresenter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void initData() {
        DaggerMyPublishQuestionComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myPublishQuestionPresenterModule(new MyPublishQuestionPresenterModule(this))
                .build()
                .inject(this);
        super.initData();
    }
}

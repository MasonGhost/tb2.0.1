package com.zhiyicx.thinksnsplus.modules.q_a.mine.answer;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity.BUNDLE_MY_QUESTON_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerFragment extends TSListFragment<MyAnswerContract.Presenter, AnswerInfoBean>
        implements MyAnswerContract.View{

    @Inject
    MyAnswerPresenter mAnswerPresenter;

    private String mType;

    public MyAnswerFragment instance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MY_QUESTON_TYPE, type);
        MyAnswerFragment fragment = new MyAnswerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void initData() {
        DaggerMyAnswerComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myAnswerPresenterModule(new MyAnswerPresenterModule(this))
                .build()
                .inject(this);
        mType = getArguments().getString(BUNDLE_MY_QUESTON_TYPE);
        super.initData();
    }

    @Override
    public String getType() {
        return mType;
    }
}

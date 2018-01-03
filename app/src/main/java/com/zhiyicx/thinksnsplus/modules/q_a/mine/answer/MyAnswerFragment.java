package com.zhiyicx.thinksnsplus.modules.q_a.mine.answer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity.BUNDLE_MY_QUESTION_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerFragment extends TSListFragment<MyAnswerContract.Presenter, AnswerInfoBean>
        implements MyAnswerContract.View {

    @Inject
    MyAnswerPresenter mAnswerPresenter;

    private String mType;

    public static MyAnswerFragment instance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MY_QUESTION_TYPE, type);
        MyAnswerFragment fragment = new MyAnswerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MyAnswerAdapterV2 answerAdapter = new MyAnswerAdapterV2(getContext(), mListDatas, mAnswerPresenter);
        answerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                AnswerInfoBean answerInfoBean = mListDatas.get(position);
                if (answerInfoBean != null) {
                    Intent intent = new Intent(getContext(), AnswerDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_ANSWER, answerInfoBean);
                    bundle.putLong(BUNDLE_SOURCE_ID, answerInfoBean.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }


            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return answerAdapter;
    }

    @Override
    protected void initView(View rootView) {

        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_MY_QUESTION_TYPE);
        }
        super.initView(rootView);
        Observable.create(subscriber -> {

            DaggerMyAnswerComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .myAnswerPresenterModule(new MyAnswerPresenterModule(MyAnswerFragment.this))
                    .build()
                    .inject(MyAnswerFragment.this);

            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

    }


    @Override
    public String getType() {
        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_MY_QUESTION_TYPE);
        }
        return mType;
    }

    @Override
    public void updateList(int position, AnswerInfoBean answerInfoBean) {
        mListDatas.set(position, answerInfoBean);
        refreshData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }
}

package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.DaggerMinePresenterComponent;
import com.zhiyicx.thinksnsplus.modules.home.mine.MineFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.MinePresenterModule;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QAListInfoAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyPublishQuestionFragment extends TSListFragment<MyPublishQuestionContract.Presenter, QAListInfoBean>
        implements MyPublishQuestionContract.View {

    public static final String MY_QUESTION_TYPE = "MY_QUESTION_TYPE";
    public static final String MY_QUESTION_TYPE_ALL = "all";// 全部
    public static final String MY_QUESTION_TYPE_INVITATION = "invitation";// 邀请
    public static final String MY_QUESTION_TYPE_REWARD = "reward";// 悬赏
    public static final String MY_QUESTION_TYPE_OTHER = "other";// 其它

    @Inject
    MyPublishQuestionPresenter mPublishQuestionPresenter;

    public static MyPublishQuestionFragment getInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(MY_QUESTION_TYPE, type);
        MyPublishQuestionFragment myPublishQuestionFragment = new MyPublishQuestionFragment();
        myPublishQuestionFragment.setArguments(bundle);
        return myPublishQuestionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(subscriber -> {
            DaggerMyPublishQuestionComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .myPublishQuestionPresenterModule(new MyPublishQuestionPresenterModule(MyPublishQuestionFragment.this))
                    .build()
                    .inject(MyPublishQuestionFragment.this);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    initData();
                }, Throwable::printStackTrace);
    }

    @Override
    protected boolean showToolbar() {
        return false;
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
    public String getMyQuestionType() {
        return getArguments().getString(MY_QUESTION_TYPE, "");
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        QAListInfoAdapter adapter = new QAListInfoAdapter(getActivity(), mListDatas) {
            @Override
            protected int getRatio() {
                return mPresenter.getRatio();
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, mListDatas.get(position));
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            super.initData();
        }
    }
}

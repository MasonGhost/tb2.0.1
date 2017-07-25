package com.zhiyicx.thinksnsplus.modules.q_a.publish_question;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QA_LIstInfoBean;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscriber;

import static com.zhiyicx.common.config.ConstantConfig.SEARCH_JITTER_SPACING_TIME;


/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class PublishQuestionFragment extends TSListFragment<PublishQuestionContract.Presenter, QA_LIstInfoBean> implements PublishQuestionContract.View {

    @BindView(R.id.et_qustion)
    EditText mEtQustion;

    private String mQuestionStr;

    public static PublishQuestionFragment newInstance() {

        Bundle args = new Bundle();
        PublishQuestionFragment fragment = new PublishQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_qustion;
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish);
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        RxTextView.afterTextChangeEvents(mEtQustion)
                .compose(this.bindToLifecycle())
                .throttleFirst(SEARCH_JITTER_SPACING_TIME, TimeUnit.MILLISECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Subscriber<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mToolbarRight.setEnabled(false);
                    }

                    @Override
                    public void onNext(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        mQuestionStr = textViewAfterTextChangeEvent.editable().toString().trim();
                        if (!TextUtils.isEmpty(mQuestionStr)) {
                            // TODO: 2017/7/25  搜索相同的問題
                            mToolbarRight.setEnabled(true);
                        }else {
                            mToolbarRight.setEnabled(false);
                        }

                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < 10; i++) {
            QA_LIstInfoBean qa_lIstInfoBean = new QA_LIstInfoBean();
            mListDatas.add(qa_lIstInfoBean);
        }
        refreshData();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        PublishQuestionAdapter adapter = new PublishQuestionAdapter(getContext(), R.layout.item_publish_question, mListDatas);
        return adapter;
    }

}

package com.zhiyicx.thinksnsplus.modules.q$a.publish_question;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;


/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class PublishQuestionFragment extends TSFragment<PublishQuestionContract.Presenter> implements PublishQuestionContract.View {


    @BindView(R.id.et_qustion)
    EditText mEtQustion;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.rv_questions)
    RecyclerView mRvQuestions;

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
        return super.setLeftTitle();
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

}

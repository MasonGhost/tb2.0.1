package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentFragment extends TSListFragment<QuestionCommentContract.Presenter, QuestionCommentBean>
        implements QuestionCommentContract.View{

    private QAListInfoBean mQaListInfoBean;

    public QuestionCommentFragment instance(Bundle bundle){
        QuestionCommentFragment fragment = new QuestionCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public QAListInfoBean getCurrentQuestion() {
        return mQaListInfoBean;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}

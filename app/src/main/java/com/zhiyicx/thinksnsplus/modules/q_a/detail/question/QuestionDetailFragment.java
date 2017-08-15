package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailFragment extends TSListFragment<QuestionDetailContract.Presenter, AnswerInfoBean>
        implements QuestionDetailContract.View{

    public QuestionDetailFragment instance(Bundle bundle){
        QuestionDetailFragment fragment = new QuestionDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}

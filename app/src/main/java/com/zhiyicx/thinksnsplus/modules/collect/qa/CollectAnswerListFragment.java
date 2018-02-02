package com.zhiyicx.thinksnsplus.modules.collect.qa;

import android.os.Bundle;

import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerFragment;

import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity.BUNDLE_MY_QUESTION_TYPE;

/**
 * @Author Jliuer
 * @Date 2018/01/04/10:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CollectAnswerListFragment extends MyAnswerFragment {

    public static CollectAnswerListFragment instance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MY_QUESTION_TYPE, type);
        CollectAnswerListFragment fragment = new CollectAnswerListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}

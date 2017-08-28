package com.zhiyicx.thinksnsplus.modules.q_a.mine.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerContainerFragment extends TSViewPagerFragment{

    public static final String TYPE_ALL = "all";
    public static final String TYPE_NOT_READ = "not_read";
    public static final String TYPE_INVITE = "invite";
    public static final String TYPE_ADOPTION = "adoption";
    public static final String TYPE_OTHER = "other";

    private List<Fragment> mFragments;

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.qa_mine_answer_title));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null){
            mFragments = new ArrayList<>();
        }
        mFragments.add(new MyAnswerFragment().instance(TYPE_ALL));
        mFragments.add(new MyAnswerFragment().instance(TYPE_NOT_READ));
        mFragments.add(new MyAnswerFragment().instance(TYPE_INVITE));
        mFragments.add(new MyAnswerFragment().instance(TYPE_ADOPTION));
        mFragments.add(new MyAnswerFragment().instance(TYPE_OTHER));
        return mFragments;
    }

    @Override
    protected void initData() {

    }
}

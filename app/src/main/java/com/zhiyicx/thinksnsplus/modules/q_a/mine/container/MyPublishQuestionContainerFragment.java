package com.zhiyicx.thinksnsplus.modules.q_a.mine.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment;

import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_ALL;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_INVITATION;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_OTHER;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_REWARD;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyPublishQuestionContainerFragment extends TSViewPagerFragment {


    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.qa_mine_publish_question_title));
    }

    @Override
    protected List<Fragment> initFragments() {
        Fragment allFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_ALL);
        Fragment invitationFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_INVITATION);
        Fragment rewardFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_REWARD);
        Fragment otherFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_OTHER);
        return Arrays.asList(allFragment, invitationFragment, rewardFragment, otherFragment);
    }

    @Override
    protected void initData() {

    }
}

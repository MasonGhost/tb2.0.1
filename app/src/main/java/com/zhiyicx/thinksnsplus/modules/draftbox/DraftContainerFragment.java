package com.zhiyicx.thinksnsplus.modules.draftbox;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/11/21/10:05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DraftContainerFragment extends TSViewPagerFragment {

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.draft_type_questions), getString(R.string.draft_type_answers),getString(R.string.draft_type_circle));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment questionDraft = DraftBoxFragment.getInstance(DraftBoxFragment.MY_DRAFT_TYPE_QUESTION);
            Fragment answerDraft = DraftBoxFragment.getInstance(DraftBoxFragment.MY_DRAFT_TYPE_ANSWER);
            Fragment postDraft = DraftBoxFragment.getInstance(DraftBoxFragment.MY_DRAFT_TYPE_POST);
            mFragmentList = Arrays.asList(questionDraft, answerDraft,postDraft);
        }
        return mFragmentList;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.draft_box);
    }

    @Override
    protected void initData() {

    }
}

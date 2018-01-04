package com.zhiyicx.thinksnsplus.modules.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.collect.album.CollectAlbumListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.dynamic.CollectDynamicListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.group_posts.CollectCirclePostListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.info.CollectInformationListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.qa.CollectAnswerListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.qa.CollectQustionLIstFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyAnswerContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QA_ListInfoFragment;

import java.util.Arrays;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectListFragment extends TSViewPagerFragment<CollectListPresenter> {
    @Override
    protected List<String> initTitles() {

        return Arrays.asList(
                getString(R.string.collect_dynamic)
                , getString(R.string.collect_info)
                , getString(R.string.collect_album)
                , getString(R.string.collect_post)
//                , getString(R.string.collect_question)
                , getString(R.string.collect_answer)
        );
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int getOffsetPage() {
        return 5;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.collect);
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment dynamicListFragment = CollectDynamicListFragment.newInstance();
            Fragment infoListFragment = CollectInformationListFragment.newInstance();
            Fragment albumListFragment = CollectAlbumListFragment.newInstance();
            Fragment postListFragment = CollectCirclePostListFragment.newInstance(BaseCircleRepository.CircleMinePostType.COLLECT);
//            Fragment questionListFragment = CollectQustionLIstFragment.newInstance(QA_ListInfoFragment.QuestionType.FOLLOW.value);
            Fragment answerListFragment = CollectAnswerListFragment.instance(MyAnswerContainerFragment.TYPE_FOLLOW);
            mFragmentList = Arrays.asList(
                    dynamicListFragment
                    , infoListFragment
                    , albumListFragment
                    , postListFragment
//                    , questionListFragment
                    , answerListFragment
            );
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    public static CollectListFragment newInstance(Bundle bundle) {
        CollectListFragment collectListFragment = new CollectListFragment();
        collectListFragment.setArguments(bundle);
        return collectListFragment;
    }
}

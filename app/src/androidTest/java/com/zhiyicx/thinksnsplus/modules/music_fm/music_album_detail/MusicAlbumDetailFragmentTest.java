package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicAlbumDetailFragmentTest {

    private ViewInteraction music_list, bt_goto;

    @Rule
    public ActivityTestRule<MusicDetailActivity> mActivityRule =
            new ActivityTestRule<MusicDetailActivity>(MusicDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, MusicDetailActivity.class);
                    Bundle bundle = new Bundle();
                    MusicAlbumListBean messageItemBean = new MusicAlbumListBean();
                    messageItemBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
                    messageItemBean.setCollect_count(5);
                    messageItemBean.setStorage(new MusicAlbumListBean().getStorage());
                    messageItemBean.setTitle("addBtnAnimation");
                    messageItemBean.setId(100);
                    messageItemBean.setMaxId(System.currentTimeMillis());
                    bundle.putParcelable(MusicListFragment.BUNDLE_MUSIC_ABLUM, messageItemBean);
                    result.putExtra(MusicListFragment.BUNDLE_MUSIC_ABLUM, bundle);
                    return result;
                }
            };

    @Before
    public void initActivity() {
        music_list = onView(withId(R.id.rv_music_detail_list));
        bt_goto = onView(withId(R.id.fragment_back_content));
    }

    @Ignore
    @Test
    public void playMusic() {
        music_list.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    public void testGotoMusicDetail() {
        playMusic();
        bt_goto.perform(click());
    }

}

package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;

import org.junit.Before;
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
    public ActivityTestRule<MusicDetailActivity> mActivityRule = new ActivityTestRule
            (MusicDetailActivity.class);

    @Before
    public void initActivity() {
        music_list = onView(withId(R.id.rv_music_detail_list));
        bt_goto = onView(withId(R.id.fragment_back_content));
    }

    @Test
    public void playMusic() {
        music_list.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    public void testGotoMusicDetail() {
        playMusic();
        bt_goto.perform(click());
    }

}

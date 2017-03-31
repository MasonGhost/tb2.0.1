package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * @Author Jliuer
 * @Date 2017/02/21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListFragmentTest extends AcitivityTest {

    private ViewInteraction album_list;

    @Rule
    public ActivityTestRule<MusicListActivity> mActivityRule = new ActivityTestRule
            (MusicListActivity.class);

    @Before
    public void initActivity() {
        album_list = onView(withId(R.id.swipe_target));
    }

    @Test
    public void testGotoMusicAlbumDetail() {
        album_list.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}

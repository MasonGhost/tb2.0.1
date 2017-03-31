package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPlayFragmentTest extends AcitivityTest {

    private ViewInteraction bt_order, bt_palyOrPause, bt_previous, bt_next;

    @Rule
    public ActivityTestRule<MusicPlayActivity> mActivityRule = new ActivityTestRule
            (MusicPlayActivity.class);

    @Before
    public void initActivity() {
        bt_order = onView(withId(R.id.fragment_music_paly_order));
        bt_palyOrPause = onView(withId(R.id.fragment_music_paly_palyer));
        bt_previous = onView(withId(R.id.fragment_music_paly_preview));
        bt_next = onView(withId(R.id.fragment_music_paly_nextview));
    }

    @Test
    public void testInit() {
        assertTrue(true);
    }

//    @Test
//    public void testPlayOrPauseMusic() {
//        bt_palyOrPause.perform(click());
//    }
//
//    @Test
//    public void testChangeOrder() {
//        bt_order.perform(click());
//    }
//
//    @Test
//    public void testPreMusic() {
//        bt_previous.perform(click());
//    }
//
//    @Test
//    public void testNextMusic() {
//        bt_next.perform(click());
//    }
}

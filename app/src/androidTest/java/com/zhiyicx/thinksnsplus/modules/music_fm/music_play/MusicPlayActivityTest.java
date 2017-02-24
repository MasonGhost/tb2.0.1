package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Rule;
import org.junit.Test;

/**
 * @Author Jliuer
 * @Date 2017/02/21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPlayActivityTest extends AcitivityTest {
    @Rule
    public ActivityTestRule<MusicPlayActivity> mActivityRule = new ActivityTestRule
            (MusicPlayActivity.class);
 // 没有测试方法，无法通过，故添加了一个
    @Test
    public void testInit(){}

}

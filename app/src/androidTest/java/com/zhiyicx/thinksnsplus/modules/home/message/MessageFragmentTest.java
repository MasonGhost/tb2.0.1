package com.zhiyicx.thinksnsplus.modules.home.message;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/2
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragmentTest {
    @Rule
    public ActivityTestRule<GuideActivity> mActivityRule = new ActivityTestRule(GuideActivity.class);


    @Test
    public void init() {
        try {
            MessageFragment messageFragment = MessageFragment.newInstance();
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

    }

}
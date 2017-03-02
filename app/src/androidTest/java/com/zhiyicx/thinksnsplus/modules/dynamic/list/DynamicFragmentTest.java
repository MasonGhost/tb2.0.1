package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/23
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DynamicFragmentTest {
    @Rule
    public ActivityTestRule<GuideActivity> mActivityRule = new ActivityTestRule(GuideActivity.class);

    @Before
    public void initActivity() {

    }

    /**
     * summary
     * steps
     * expected
     *
     * @throws Exception
     */
    @Test
    public void testDynamicActivity() throws Exception {
//        onView(withId(R.id.tsv_toolbar)).check(matches(isDisplayed()));
    }

    /**
     * summary              当主页显示的时候
     * steps                查看首页类容
     * expected              首页分类正常显示
     *
     * @throws Exception
     */
    @Test
    public void testHomeNoBack() throws Exception {
//        onView(withId(R.id.mg_indicator)).check(matches(isDisplayed()));
    }

}
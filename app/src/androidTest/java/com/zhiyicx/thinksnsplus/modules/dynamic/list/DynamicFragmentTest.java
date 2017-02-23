package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

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
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule(HomeActivity.class);

    @Before
    public void initActivity() {
//        RxUnitTestTools.openRxTools();
//        mRegisterClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getRegisterClient();

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
        DynamicFragment dynamicFragment=DynamicFragment.newInstance();
        dynamicFragment.showMessage("this is a test message");
    }

}
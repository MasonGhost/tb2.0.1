package com.zhiyicx.thinksnsplus.modules.home;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */
public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<HomeActivity>(HomeActivity.class);

    /**
     * summary                       初始化首页
     * steps                         初始化首页
     * expected                     底部导航栏正常显示
     *
     * @throws Exception
     */
    @Test
    public void init() {
        onView(withId(R.id.tv_home)).check(matches(withText(mActivityRule.getActivity().getString(R.string.home))));
    }
}
package com.zhiyicx.thinksnsplus.modules.register;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/23
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule(RegisterActivity.class);

    /**
     * summary                      不输入用户名
     * steps                         不输入昵称点击注册
     * expected result               按钮颜色不亮，无法点击
     * @throws Exception
     */
    @Test
    public void notInputUsername() throws Exception {
        onView(withId(R.id.tv_test)).check(matches(withText("Hello blank fragment")));
    }

}
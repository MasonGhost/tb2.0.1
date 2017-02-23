<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.thinksnsplus.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/5
 * @Contact master.jungle68@gmail.com
 */
public class AboutUsActivityTest {
    AboutUsFragment mAboutUsFragment;
    @Rule
    public ActivityTestRule<AboutUsActivity> mActivityRule = new ActivityTestRule(AboutUsActivity.class);

    @Before
    public void init() {
        mAboutUsFragment = mActivityRule.getActivity().getFragment();
    }

    @Test
    public void loadUrl() throws Exception {
        if (NetUtils.netIsConnected(mActivityRule.getActivity())) {
            Thread.sleep(100);
            onView(withId(R.id.pb_bar)).check(matches((isDisplayed())));
        }
    }

    @Test
    public void setProgerss() {
        mAboutUsFragment.setNeedProgress(true);
        Assert.assertTrue(mAboutUsFragment.isNeedProgress());
    }


=======
package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.thinksnsplus.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/5
 * @Contact master.jungle68@gmail.com
 */
public class AboutUsActivityTest {
    AboutUsFragment mAboutUsFragment;
    @Rule
    public ActivityTestRule<AboutUsActivity> mActivityRule = new ActivityTestRule(AboutUsActivity.class);

    @Before
    public void init() {
        mAboutUsFragment = mActivityRule.getActivity().getFragment();
    }

    @Test
    public void loadUrl() throws Exception {
        if (NetUtils.netIsConnected(mActivityRule.getActivity())) {
            Thread.sleep(100);
            onView(withId(R.id.pb_bar)).check(matches((isDisplayed())));
        }
    }

    @Test
    public void setProgerss() {
        mAboutUsFragment.setNeedProgress(true);
        Assert.assertTrue(mAboutUsFragment.isNeedProgress());
    }


>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
}
package com.zhiyicx.thinksnsplus.modules;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/30
 * @Contact master.jungle68@gmail.com
 */

public final class MyViewMatchers {
    /**
     * Returns a matcher that matches {@link View}s that are disenabled.
     */
    public static Matcher<View> disEnabled() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is disable");
            }

            @Override
            public boolean matchesSafely(View view) {
                return !view.isEnabled();
            }
        };
    }
    /**
     * Returns a matcher that matches {@link View}s that are disclickable.
     */
    public static Matcher<View> disClickable() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is disclickable");
            }

            @Override
            public boolean matchesSafely(View view) {
                return !view.isClickable();
            }
        };
    }

}

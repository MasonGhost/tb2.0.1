package com.zhiyicx.thinksnsplus.modules;

import android.graphics.Rect;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

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
     * Returns a matcher that matches {@link View}s that are clickable.
     */
    public static Matcher<View> isUnClickable() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return !item.isClickable();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is unclickable");
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

    /**
     * Returns a matcher that matches {@link View}s that are currently not displayed on the screen to the
     * user.
     *
     * Note: isDisplayed will select views that are partially displayed (eg: the full height/width of
     * the view is greater then the height/width of the visible rectangle). If you wish to ensure the
     * entire rectangle this view draws is displayed to the user use isCompletelyDisplayed.
     */
    public static Matcher<View> isDisappear() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is not displayed on the screen to the user");
            }

            @Override
            public boolean matchesSafely(View view) {
                return !(view.getGlobalVisibleRect(new Rect())
                        && withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE).matches(view));
            }
        };
    }

    /**
     * Returns a matcher that matches {@link View}s currently have no focus.
     */
    public static Matcher<View> notFcused() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("has no focus on the screen to the user");
            }

            @Override
            public boolean matchesSafely(View view) {
                return !view.hasFocus();
            }
        };
    }


}

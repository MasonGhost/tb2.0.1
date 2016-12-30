package com.zhiyicx.thinksnsplus.modules;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/30
 * @Contact master.jungle68@gmail.com
 */

public class DisableMatcher<P extends View> extends TypeSafeMatcher<P> {
    @Override
    protected boolean matchesSafely(P item) {
        return !item.isEnabled();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is disabled");
    }
}

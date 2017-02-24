package com.zhiyicx.thinksnsplus.modules;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.R;

import org.junit.runner.RunWith;

import static android.R.attr.description;
import static android.R.attr.type;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AcitivityTest {

    /**
     * 清空输入框的内容
     *
     * @param viewInteraction
     */
    protected void clearEditText(ViewInteraction... viewInteraction) {
        if (viewInteraction != null) {
            for (ViewInteraction interaction : viewInteraction) {
                interaction.perform(clearText());
            }
        }
    }

    /**
     * 根据控件id获取控件
     *
     * @param viewId
     * @return
     */
    protected ViewInteraction findViewById(int viewId) {
        return onView(withId(viewId));
    }

    /**
     * 获取一定长度的字符串
     */
    protected String getLengthString(int length) {
        String a = "";
        for (int i = 0; i < length; i++) {
            a += "帅";
        }
        return a;

    }

}

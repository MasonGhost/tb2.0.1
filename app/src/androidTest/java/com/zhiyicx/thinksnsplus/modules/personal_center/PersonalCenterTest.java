package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/16
 * @contact email:450127106@qq.com
 */

public class PersonalCenterTest extends AcitivityTest {
    private ViewInteraction iv_background_cover;// 背景
    private ViewInteraction ll_follow_container;// 关注
    private ViewInteraction ll_chat_container;// 聊天


    @Rule
    public ActivityTestRule<PersonalCenterActivity> mActivityRule = new ActivityTestRule(PersonalCenterActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            //模拟要发送的activity
            Bundle bundle = new Bundle();
            UserInfoBean mUserInfoBean = new UserInfoBean();
            UserInfoBean.UserInfoExtraBean extraBean = new UserInfoBean.UserInfoExtraBean();
            mUserInfoBean.setExtra(extraBean);
            mUserInfoBean.setUser_id(6L);
            mUserInfoBean.setCover(20 + "");
            mUserInfoBean.setAvatar(10 + "");
            mUserInfoBean.setName("haha");
            mUserInfoBean.setIntro("hahahahhaha");
            mUserInfoBean.getExtra().setFollowings_count(20);
            mUserInfoBean.getExtra().setFollowers_count(30);
            bundle.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, mUserInfoBean);
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, PersonalCenterActivity.class);
            intent.putExtras(bundle);
            return intent;
        }
    };

    @Before
    public void init() {
        iv_background_cover = findViewById(R.id.iv_background_cover);
        ll_follow_container = findViewById(R.id.ll_follow_container);
        ll_chat_container = findViewById(R.id.ll_chat_container);
    }

    /**
     * summary 是否可以拍照更换个人背景
     * steps   背景图片是否可点击
     * expected 背景图片可点击
     */
    @Test
    public void canChangeCoverByCamera() throws Exception {
        iv_background_cover.check(matches(isClickable()));
    }

    /**
     * summary	是否可以选择照片更换背景
     * steps  背景图片是否可点击
     * expected 背景图片可点击
     */
    @Test
    public void canChangeCoverByAlbum() throws Exception {
        iv_background_cover.check(matches(isClickable()));
    }

    /**
     * 个人主页是否可以进入粉丝列表
     *
     * @throws Exception
     */
    @Ignore
    public void canGoToFansList() throws Exception {

    }

    /**
     * 个人主页是否可以点击查看关注列表
     *
     * @throws Exception
     */
    @Ignore
    public void canGoToFollowList() throws Exception {

    }

    /**
     * summary 他人个人主页是否可以关注
     * steps  关注按钮可点击
     * expected 可点击
     */
    @Test
    public void canFollowInOtherPeopleCenter() throws Exception {
        ll_follow_container.check(matches(isDisplayed())); // 关注按钮可见
        ll_follow_container.check(matches(isClickable()));// 关注按钮可点击
    }

    /**
     * summary 他人个人主页是否可以点击进入聊天
     * steps
     * expected
     */
    @Test
    public void canGoToChat() throws Exception {
        ll_chat_container.check(matches(isDisplayed())); // 聊天按钮可见
        ll_chat_container.check(matches(isClickable()));// 聊天按钮可点击

    }
}

package com.zhiyicx.thinksnsplus.modules.followfanslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/16
 * @contact email:450127106@qq.com
 */

public class FollowFansListTest extends AcitivityTest {

    private ViewInteraction swipe_target;
    private ViewInteraction vp_fragment;// viewpager

    @Rule
    public ActivityTestRule<FollowFansListActivity> mActivityRule = new ActivityTestRule(FollowFansListActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            //模拟要发送的activity
            Bundle bundleFans = new Bundle();
            bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, FollowFansListActivity.class);
            intent.putExtras(bundleFans);
            return intent;
        }
    };

    @Before
    public void init() {
        vp_fragment = findViewById(R.id.vp_fragment);
    }

    /**
     * summary 是否显示用户信息
     * steps
     * expected
     */
    @Test
    public void showHeadInfo() throws Exception {
        vp_fragment.perform(swipeRight());// viewpager滑动到第一页
        Thread.sleep(2000);// 等待滑动结束
        vp_fragment = onView(allOf(withId(R.id.swipe_target), isDisplayed()));// 获取viewpager中可见的fragment中的recyclerview
        //vp_fragment.perform(MyViewAction.addChildData(0));
        //vp_fragment.perform(RecyclerViewActions.actionOnItemAtPosition(0,MyViewAction.clickChildViewWithId(R.id.tv_user_name)));
        //  vp_fragment.check(matches(isUserInfoVisible()));
    }

    /**
     * summary 是否显示点赞数量
     * steps
     * expected
     */
    @Ignore
    public void showDigCount() throws Exception {

    }

    /**
     * summary 点击头像，名称是否可以跳转到用户个人中心
     * steps
     * expected 跳转到用户个人中心
     */
    @Ignore
    public void clickHeadAndNameToPersonalCenter() throws Exception {

    }


    /**
     * summary 点击关注按钮状态发生变化
     * steps
     * expected
     */
    @Ignore
    public void canClickFollowButton() throws Exception {

    }

    /**
     * summary 点击关注粉丝列表中的未关注用户，关注列表增加一个关注用户
     * steps
     * expected
     */
    @Ignore
    public void clickFansListFollow() throws Exception {

    }

    /**
     * 用户信息是否可见
     *
     * @return
     */
    private static Matcher<View> isUserInfoVisible() {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                EmptyWrapper<FollowFansBean> adapter = (EmptyWrapper) recyclerView.getAdapter();
                CommonAdapter<FollowFansBean> innerAdapter = (CommonAdapter<FollowFansBean>) adapter.getInnerAdapter();
                List<FollowFansBean> followList = innerAdapter.getDatas();
                FollowFansBean followFansBean = new FollowFansBean();
                followFansBean.setOriginUserId(5);
                followFansBean.setTargetUserId(6);
                followFansBean.setOrigintargetUser("");
                followFansBean.setOrigin_follow_status(1);
                followFansBean.setTarget_follow_status(1);
                UserInfoBean targetInfo = new UserInfoBean();
                targetInfo.setAvatar(10 + "");
                targetInfo.setIntro("hahahha");
                targetInfo.setName("fdsafda");
                followFansBean.setTargetUserInfo(targetInfo);
                followList.add(followFansBean);
                adapter.notifyDataSetChanged();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                View v = recyclerView.getChildAt(0);
                boolean headIsVisible = v.findViewById(R.id.iv_headpic).getVisibility() == View.VISIBLE;
                boolean tv_name = v.findViewById(R.id.tv_name).getVisibility() == View.VISIBLE;
                boolean tv_user_signature = v.findViewById(R.id.tv_user_signature).getVisibility() == View.VISIBLE;
                return headIsVisible && tv_name && tv_user_signature;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("userinfo is visible: ");
            }
        };
    }

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(View.class);
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                    System.out.print("perform" + v.getClass().toString());
                }
            };
        }

        public static ViewAction addChildData(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(View.class);
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    EmptyWrapper<FollowFansBean> adapter = (EmptyWrapper) recyclerView.getAdapter();
                    CommonAdapter<FollowFansBean> innerAdapter = (CommonAdapter<FollowFansBean>) adapter.getInnerAdapter();
                    List<FollowFansBean> followList = innerAdapter.getDatas();
                    FollowFansBean followFansBean = new FollowFansBean();
                    followFansBean.setOriginUserId(5);
                    followFansBean.setTargetUserId(6);
                    followFansBean.setOrigintargetUser("");
                    followFansBean.setOrigin_follow_status(1);
                    followFansBean.setTarget_follow_status(1);
                    UserInfoBean targetInfo = new UserInfoBean();
                    targetInfo.setAvatar(10 + "");
                    targetInfo.setIntro("hahahha");
                    targetInfo.setName("fdsafda");
                    followFansBean.setTargetUserInfo(targetInfo);
                    followList.add(followFansBean);
                    adapter.notifyDataSetChanged();
                }
            };
        }

    }
}

package com.zhiyicx.thinksnsplus.modules.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.disEnabled;

/**
 * @Describe 聊天详情页面测试
 * @Author Jungle68
 * @Date 2017/3/2
 * @Contact master.jungle68@gmail.com
 */
public class ChatFragmentTest {
    // 保证启动 ChatActivity 的时候都会传递一下数据
    @Rule
    public ActivityTestRule<ChatActivity> mActivityRule =
            new ActivityTestRule<ChatActivity>(ChatActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    MessageItemBean messageItemBean = new MessageItemBean();
                    Conversation conversation = new Conversation();
                    conversation.setIm_uid(1);
                    conversation.setCid(1);
                    messageItemBean.setConversation(conversation);
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setName("jungle");
                    messageItemBean.setUserInfo(userInfoBean);
                    bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
                    result.putExtras(bundle);
                    return result;
                }
            };

    @Before
    public void initActivity() {
    }

    /**
     * summary                       测试启动聊天
     * steps                         进入聊天页面
     * expected                      正常显示聊天名字
     *
     * @throws Exception
     */
    @Test
    public void lunchActvity() throws Exception {
        Bundle bundle = new Bundle();
        MessageItemBean messageItemBean = new MessageItemBean();
        Conversation conversation = new Conversation();
        conversation.setIm_uid(1);
        conversation.setCid(1);
        messageItemBean.setConversation(conversation);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setName("jungle");
        messageItemBean.setUserInfo(userInfoBean);
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, bundle);

        // There maybe more than one app will handle this requirement
        String packageName = InstrumentationRegistry.getTargetContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName,
                ChatActivity.class.getName());
        intent.setComponent(componentName);

        Intents.init();
        InstrumentationRegistry.getContext().startActivity(intent);
        Matcher<Intent> expectedIntent = hasComponent(componentName);
        intended(expectedIntent);
        Intents.release();

        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.tv_toolbar_center))
                .check(matches(withText("jungle")));
    }

    /**
     * summary                       点击和 jungle 的对话列表
     * steps                         进入聊天详情
     * expected                      聊天对象是 jungle
     *
     * @throws Exception
     */
    @Test
    public void testChatName() throws Exception {
        onView(withId(R.id.tv_toolbar_center)).check(matches(withText("jungle")));
    }

    /**
     * summary                       单聊发消息输入框为空是否可以发送
     * steps                         1.A登录 2.进入与B聊天对话框 3.不输入内容点击发送
     * expected                      发送按钮不可用
     *
     * @throws Exception
     */
    @Test
    public void testNullInput() throws Exception {
        onView(withId(R.id.et_content)).perform(typeText(""));
        onView(withId(R.id.bt_send)).check(matches(disEnabled()));
    }

    /**
     * summary                      单聊发消息空格是否可以发送
     * steps                         1.A登录 2.进入与B聊天对话框 3.不输入内容点击发送
     * expected                      发送按钮不可用
     *
     * @throws Exception
     */
    @Test
    public void testTirmInput(){
        onView(withId(R.id.et_content)).perform(replaceText("    "));
        onView(withId(R.id.bt_send)).check(matches(disEnabled()));
    }

    /**
     * summary                      单聊发消息为 i am jungle 是否可以发送
     * steps                         1.A登录 2.进入与B聊天对话框 3.不输入内容点击发送
     * expected                      发送按钮可用
     *
     * @throws Exception
     */
    @Test
    public void testTextInput(){

        onView(withId(R.id.et_content)).perform(replaceText("i am jungle"));
        onView(withId(R.id.bt_send)).check(matches(isEnabled()));
    }
}
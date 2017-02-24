package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/23
 * @contact email:450127106@qq.com
 */

public class SendDynamicTest extends AcitivityTest {

    private ViewInteraction et_content, et_titile, tv_limit_tip, tv_toolbar_right, photo_recylerView, dialog, localPhotoItem, cameraItem;
    /**
     * 当前测试启动发送动态页面，本来应该传入发送动态的类型：
     * 1.纯文字动态 ：TEXT_ONLY_DYNAMIC
     * 2.图片文字动态：PHOTO_TEXT_DYNAMIC 以及选择的图片
     * 当前启动没有传入任何数据，默认接受的页面类型为 ：图片文字动态 ，没有图片
     */
    @Rule
    public ActivityTestRule<SendDynamicActivity> mActivityRule = new ActivityTestRule(SendDynamicActivity.class);

    @Before
    public void initActivity() {
        // 内容的EditText
        et_content = onView(allOf(withId(R.id.et_content), withHint(R.string.dynamic_content_hint)));
        // 标题的EditText
        et_titile = onView(allOf(withId(R.id.et_content), withHint(R.string.dynamic_title_hint)));
        // 发布按钮
        tv_toolbar_right = onView(withId(R.id.tv_toolbar_right));
        // 图片列表
        photo_recylerView = onView(withId(R.id.rv_photo_list));
        // 选择图片的弹框dialog
        dialog = onView(withText(R.string.choose_from_photo))// 控件上的文字
                // 控件不在主UI布局上
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())));

/*        // 从相册选择图片
        localPhotoItem = onView(allOf(withId(R.id.tv_pop_item1), withParent(withId(R.id.item_container)))).inRoot(isPlatformPopup());
        ;
        // 从相机选择图片
        cameraItem = onView(allOf(withId(R.id.tv_pop_item2), withParent(withId(R.id.item_container))));*/

    }

    /**
     * summary 不输入标题是否可以发布
     * steps    1 .不输入标题只输入内容
     * 2.点击发布
     * expected  	发布成功
     */
    @Test
    public void notInputTitle() throws Exception {
        // 应该是不可点击的
        tv_toolbar_right.check(matches(not(isEnabled())));
        // 输入内容，关闭输入法
        et_content.perform(replaceText("我输入了很多很多的内容，但是不输入标题"), closeSoftKeyboard());
        // 断言发布按钮可以点击
        tv_toolbar_right.check(matches(isEnabled()));
    }

    /**
     * summary 标题输入30字是否可以发布
     * steps   1.输入30字标题
     * 2.输入内容点击发布
     * expected  	发布成功
     */
    @Test
    public void titleInput30Word() throws Exception {
        String titleString = getLengthString(30);
        // 先断言输入的内容长度为30
        assertTrue(titleString.length() == 30);
        et_titile.perform(replaceText(titleString), closeSoftKeyboard());
        et_content.perform(replaceText("我输入了很多很多的内容"), closeSoftKeyboard());
        tv_toolbar_right.check(matches(isEnabled()));
    }

    /**
     * summary  标题输入超过30字
     * steps    输入标题很多文字
     * expected  输入30字就不能输入了
     */
    @Test
    public void titleInputPass30Word() throws Exception {
        String titleString = getLengthString(40);
        // 先断言输入的内容长度大于30
        assertTrue(titleString.length() > 30);
        // 输入长度大于30的文字内容
        et_titile.perform(replaceText(titleString), closeSoftKeyboard());
        // 检测标题输入框的实际内容和输入的内容不同
        et_titile.check(matches(not(withText(titleString))));
    }

    /**
     * summary  不输入内容是否可以发布
     * steps  1.输入标题“小可爱”
     * 2.不输入内容和图片
     * 3.点击发布
     * expected  发布按钮不亮，无法点击
     */
    @Test
    public void notInputContent() throws Exception {
        et_titile.perform(replaceText("小可爱"), closeSoftKeyboard());
        tv_toolbar_right.check(matches(not(isEnabled())));
    }

    /**
     * summary  只上传图片是否可以发布
     * steps    1.不输入标题与文字内容
     * 2.上传一张图片
     * 3.点击发布
     * expected  发布成功
     */
    @Ignore
    public void justAddPhotos() throws Exception {
        // 此时没有图片，点击第一个item，启动弹框
       // photo_recylerView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
       // dialog.perform();
        // 跳转到图片选择页面
        //localPhotoItem.perform(click());
        //onView(withId(R.id.rv_album_details)).check(matches(isDisplayed()));

    }

    /**
     * summary  是否可以从相册选择上传图片
     * steps   1.点击图片占位
     * 2.弹窗选择“从相册中选择”
     * 3.进入相册选择照片确认
     * expected  照片预览在发布动态页面
     */
    @Ignore
    public void canSelectLocalPhoto() throws Exception {


    }

    /**
     * summary  是否可以拍照选择上传图片
     * steps  1.点击图片占位
     * 2.弹窗选择“相机” 3.拍照编辑确认
     * expected  照片预览在发布动态页面
     */
    @Ignore
    public void canSelectCameraPhoto() throws Exception {


    }

    /**
     * summary 是否可以上传超过9张照片
     * steps  1.点击图片占位
     * 2.弹窗点击“从相册中选择”
     * 3.点击多张图片
     * expected  选择9张后不能继续在选择
     */
    @Ignore
    public void canSelectPass9Photos() throws Exception {


    }

    /**
     * summary 文字内容是否可以输入250字
     * steps  1.内容输入250字 2.点击发布
     * expected  发布成功
     */
    @Test
    public void inputContent250Word() throws Exception {
        String titleString = getLengthString(250);
        // 先断言输入的内容长度为250
        assertTrue(titleString.length() == 250);
        et_content.perform(replaceText(titleString), closeSoftKeyboard());
        tv_toolbar_right.check(matches(isEnabled()));

    }

    /**
     * summary  文字内容是否可以输入超过255字
     * steps    内容输入很多文字
     * expected  输入到255字不能继续输入
     */
    @Test
    public void inputContentPass250Word() throws Exception {
        String titleString = getLengthString(260);
        // 先断言输入的内容长度大于30
        assertTrue(titleString.length() > 250);
        // 输入长度大于30的文字内容
        et_content.perform(replaceText(titleString), closeSoftKeyboard());
        // 检测标题输入框的实际内容和输入的内容不同
        et_content.check(matches(not(withText(titleString))));

    }


    /**
     * 该方法，放到HomeFragment中测试
     * <p>
     * summary 是否可以进入发纯文字界面
     * steps  长按按钮“+"
     * expected 进入发纯文字界面
     */
    @Ignore
    public void name9() throws Exception {


    }
}

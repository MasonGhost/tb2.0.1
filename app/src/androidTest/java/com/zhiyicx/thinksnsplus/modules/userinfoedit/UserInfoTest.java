package com.zhiyicx.thinksnsplus.modules.userinfoedit;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author LiuChao
 * @describe 用户信息编辑测试
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */
public class UserInfoTest extends AcitivityTest {
    private ViewInteraction mIvHeadIcon, mEtUserName, mTvSex, mTvCity, mEtUserIntroduce, mRightBtn, mIntroduce;
    private PhotoSelectorImpl mPhotoSelector;
    @Rule
    public ActivityTestRule<UserInfoActivity> mActivityRule = new ActivityTestRule(UserInfoActivity.class);

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mIvHeadIcon = findViewById(R.id.iv_head_icon);// 用户头像
        mEtUserName = findViewById(R.id.et_user_name);
        mTvSex = findViewById(R.id.tv_sex);
        mTvCity = findViewById(R.id.tv_city);
        mEtUserIntroduce = findViewById(R.id.et_user_introduce);
        mRightBtn = findViewById(R.id.tv_toolbar_right);
        mIntroduce = findViewById(R.id.et_content);
    }

    /**
     * summary  上传图片
     * steps
     * expected
     */
    @Test
    public void changeUserHeadIcon() throws Exception {

    }

    /**
     * summary 用户名称被修改，是否能够向服务器提交修改
     * steps   1.替换成最新用户名 2.验证是否能够点击 3.
     * expected 右上角完成按钮能够点击
     */
    @Test
    public void changeUserName() throws Exception {
        mEtUserName.perform(replaceText("haha--xd"));
        mRightBtn.check(matches(isEnabled()));
    }

    /**
     * summary 性别被修改，是否能够向服务器提交修改
     * steps  1.打开性别选择窗口 2.点击选择男 3.判断当前文字被设置成男  4.判断能否提交修改
     * expected 右上角完成按钮能够点击
     */
    @Test
    public void changeSex() throws Exception {

        ViewInteraction sexContainer = findViewById(R.id.ll_sex_container);
        sexContainer.perform(click());
        findViewById(com.zhiyicx.baseproject.R.id.tv_pop_item1).perform(click());
        mTvSex.check(matches(withText("男")));
        mRightBtn.check(matches(isEnabled()));
    }

    /**
     * summary 城市被修改，是否能够向服务器提交修改
     * steps  1.打开城市选择器 2.点击确定按钮，修改城市
     * expected 右上角完成按钮能够点击
     */

    @Test
    public void changeCity() throws Exception {
        findViewById(R.id.ll_city_container).perform(click(),closeSoftKeyboard());
        findViewById(com.bigkoo.pickerview.R.id.btnSubmit).perform(click());
        mRightBtn.check(matches(isEnabled()));
    }

    /**
     * summary 用户简介被修改，是否能够像服务器提交修改
     * steps
     * expected 右上角完成按钮能够点击
     */
    @Test
    public void changeIntroduce() throws Exception {
        mIntroduce.perform(replaceText("haha--xd"));
        mEtUserIntroduce.check(matches(isEnabled()));
    }

}

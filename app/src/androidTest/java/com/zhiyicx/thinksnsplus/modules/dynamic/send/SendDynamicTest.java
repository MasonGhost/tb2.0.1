package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/23
 * @contact email:450127106@qq.com
 */

public class SendDynamicTest extends AcitivityTest {

    private ViewInteraction et_content, tv_limit_tip, tv_toolbar_right;

    @Rule
    public ActivityTestRule<SendDynamicActivity> mActivityRule = new ActivityTestRule(SendDynamicActivity.class);

    @Before
    public void initActivity() {

    }

    /**
     * summary 不输入标题是否可以发布
     * steps    1 .不输入标题只输入内容
                2.点击发布
     * expected  	发布成功
     */
    @Test
    public void notInputTitle() throws Exception {

    }

    /**
     * summary 标题输入30字是否可以发布
     * steps   1.输入30字标题
               2.输入内容点击发布
     * expected  	发布成功
     */
    @Test
    public void name() throws Exception {

    }

    /**
     * summary  标题输入超过30字
     * steps    输入标题很多文字
     * expected  输入30字就不能输入了
     */
    @Test
    public void name1() throws Exception {

    }

    /**
     * summary  不输入内容是否可以发布
     * steps  1.输入标题“小可爱”
              2.不输入内容和图片
              3.点击发布
     * expected  发布按钮不亮，无法点击
     */
    @Test
    public void name2() throws Exception {


    }

    /**
     * summary  只上传图片是否可以发布
     * steps    1.不输入标题与文字内容
                2.上传一张图片
                3.点击发布
     * expected  发布成功
     */
    @Test
    public void name3() throws Exception {


    }

    /**
     * summary  是否可以从相册选择上传图片
     * steps   1.点击图片占位
               2.弹窗选择“从相册中选择”
               3.进入相册选择照片确认
     * expected  照片预览在发布动态页面
     */
    @Test
    public void name4() throws Exception {


    }

    /**
     * summary  是否可以拍照选择上传图片
     * steps  1.点击图片占位
              2.弹窗选择“相机” 3.拍照编辑确认
     * expected  照片预览在发布动态页面
     */
    @Test
    public void name5() throws Exception {


    }

    /**
     * summary 是否可以上传超过9张照片
     * steps  1.点击图片占位
              2.弹窗点击“从相册中选择”
              3.点击多张图片
     * expected  选择9张后不能继续在选择
     */
    @Test
    public void name6() throws Exception {


    }

    /**
     * summary 文字内容是否可以输入255字
     * steps  1.内容输入255字 2.点击发布
     * expected  发布成功
     */
    @Test
    public void name7() throws Exception {


    }

    /**
     * summary  文字内容是否可以输入超过255字
     * steps    内容输入很多文字
     * expected  输入到255字不能继续输入
     */
    @Test
    public void name8() throws Exception {


    }



    /**
     * summary 是否可以进入发纯文字界面
     * steps  长按按钮“+"
     * expected 进入发纯文字界面
     */
    @Test
    public void name9() throws Exception {


    }
}

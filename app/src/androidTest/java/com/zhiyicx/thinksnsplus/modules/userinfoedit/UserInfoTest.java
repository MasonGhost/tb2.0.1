package com.zhiyicx.thinksnsplus.modules.userinfoedit;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoFragment;
import com.zhiyicx.thinksnsplus.modules.register.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe 用户信息编辑测试
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */

public class UserInfoTest extends AcitivityTest {
    private ViewInteraction mIvHeadIcon;
    private PhotoSelectorImpl mPhotoSelector;
    @Rule
    public ActivityTestRule<UserInfoActivity> mActivityRule = new ActivityTestRule(UserInfoActivity.class);

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mIvHeadIcon = findViewById(R.id.iv_head_icon);// 用户头像
    }

    /**
     * summary  上传图片
     * steps
     * expected
     */
    @Test
    public void changeUserHeadIcon() throws Exception {

    }
}

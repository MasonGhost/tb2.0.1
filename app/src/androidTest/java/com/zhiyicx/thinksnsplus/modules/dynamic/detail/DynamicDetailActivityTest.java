package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicDetailActivityTest {
    // 保证启动 ChatActivity 的时候都会传递一下数据
    @Rule
    public ActivityTestRule<DynamicDetailActivity> mActivityRule =
            new ActivityTestRule<DynamicDetailActivity>(DynamicDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, DynamicDetailActivity.class);
                    Bundle bundle = new Bundle();
                    long feed_mark =1000000000120L;
                    DynamicBean dynamicBean=new DynamicBean();
                    dynamicBean.setFeed_mark(feed_mark);
                    dynamicBean.setComments(new ArrayList<DynamicCommentBean>());
                    dynamicBean.setFeed_id(10000L);
                    dynamicBean.setDigUserInfoList(new ArrayList<FollowFansBean>());
                    dynamicBean.setDigUserInfoList(new ArrayList<FollowFansBean>());
                    dynamicBean.setFollowed(true);
                    dynamicBean.setTool(new DynamicToolBean());
                    DynamicDetailBean dynamicDetailBean=new DynamicDetailBean();
                    dynamicDetailBean.setFeed_mark(feed_mark);
                    dynamicDetailBean.setFeed_content("hello jungle68");
                    dynamicDetailBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
                    dynamicDetailBean.setFeed_title("jundle68");
                    dynamicDetailBean.setStorages(new ArrayList<ImageBean>());
                    dynamicDetailBean.setStorage_task_ids(new ArrayList<Integer>());
                    dynamicDetailBean.setContent("hello jungle68");
                    dynamicDetailBean.setLocalPhotos(new ArrayList<String>());
                    UserInfoBean userInfoBean=new UserInfoBean();
                    userInfoBean.setUser_id((long) 10);
                    userInfoBean.setName("jungle68");
                    userInfoBean.setAvatar("");
                    dynamicBean.setUserInfoBean(userInfoBean);
                    dynamicBean.setUser_id(userInfoBean.getUser_id());
                    dynamicBean.setFeed(dynamicDetailBean);
                    bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicBean);
                    bundle.putString(DYNAMIC_DETAIL_DATA_TYPE, ApiConfig.DYNAMIC_TYPE_NEW);
                    bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, 0);
                    bundle.putBoolean(LOOK_COMMENT_MORE, false);
                    result.putExtras(bundle);
                    return result;
                }
            };

    @Before
    public void initActivity() {
    }

    /**
     * summary                       进入动态详情
     * steps                         进入动态详情，动态所属名字检测
     * expected                      是 jungle68
     *
     * @throws Exception
     */
    @Test
    public void testtName() throws Exception {
        onView(withId(R.id.tv_toolbar_center)).check(matches(withText("jungle68")));
    }

}
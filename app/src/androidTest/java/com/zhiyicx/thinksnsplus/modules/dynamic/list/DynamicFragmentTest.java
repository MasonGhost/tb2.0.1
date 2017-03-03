package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.*;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/23
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DynamicFragmentTest {
    @Rule
    public ActivityTestRule<GuideActivity> mActivityRule = new ActivityTestRule(GuideActivity.class);

    @Before
    public void initActivity() {

    }

    /**
     * summary
     * steps
     * expected
     *
     * @throws Exception
     */
    @Test
    public void testDynamicActivity() throws Exception {
//        onView(withId(R.id.tsv_toolbar)).check(matches(isDisplayed()));
    }

    /**
     * summary              当主页显示的时候
     * steps                查看首页类容
     * expected              首页分类正常显示
     *
     * @throws Exception
     */
    @Test
    public void testHomeNoBack() throws Exception {
//        onView(withId(R.id.mg_indicator)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdapter() {
        List<DynamicBean> datas=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean=new DynamicBean();
            dynamicBean.setFeed_id((long) i);
            datas.add(dynamicBean);
        }
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(mActivityRule.getActivity(), datas);
        adapter.addItemViewDelegate(new DynamicListBaseItem(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForOneImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForTwoImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForThreeImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForFourImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForFiveImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForSixImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForSevenImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForEightImage(mActivityRule.getActivity()));
        adapter.addItemViewDelegate(new DynamicListItemForNineImage(mActivityRule.getActivity()));
        Assert.assertTrue(true);
    }

}
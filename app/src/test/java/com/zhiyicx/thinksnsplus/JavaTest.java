package com.zhiyicx.thinksnsplus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */

public class JavaTest {
    private static final String TAG = "JavaTest";

    /**
     * utc 时间测试
     */
    @Test
    public void collectionSortTest() {
        List<Integer> data = new ArrayList<>();
        data.add(10);
        data.add(19);
        data.add(12);
        data.add(15);
        data.add(3);
        data.add(1);
        data.add(2);
        data.add(20);
        data.add(50);
        data.add(5);
        Collections.sort(data, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        LogUtils.d(TAG, "data = " + data.toString());

        List<Integer> data2 = new ArrayList<>();

        Collections.sort(data2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        LogUtils.d(TAG, "data2 = " + data2.toString());
    }

    @Test
    public void testList() {
        List<Long> userids = new ArrayList<>();
        String test = "12,14";
        String[] testarry = test.split(",");
        userids.addAll(Arrays.asList(testarry));
        LogUtils.d(TAG, "testarry = " + userids.toString());
    }

    /**
     * 去除头部符号
     */
    @Test
    public void removeSymbolStartWith() {
        String test = ",,2,3";
        LogUtils.d(TAG, "ConvertUtils.removeSymbolStartWith(test,\",\") = " + ConvertUtils.removeSymbolStartWith(test, ","));
    }

    /**
     * 去除wei部符号
     */
    @Test
    public void removeSymbolEndWith() {
        String test = ",,2,3,,";
        LogUtils.d(TAG, "ConvertUtils.removeSymbolStartWith(test,\",\") = " + ConvertUtils.removeSymbolEndWith(test, ","));
    }

    /**
     * list 的 set 方法测试
     */
    @Test
    public void listSetTest() {
        String test = "jungle68";
        List<String> datas = new ArrayList<>();

//        datas.add(0, test); 没有数据的时候set异常
        LogUtils.d(TAG, "datas = " + datas.toString());
        try {
            datas.set(0, test);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
        for (int i = 0; i < 5; i++) {
            datas.add("test   " + i);
        }
        datas.set(0, test);
        Assert.assertTrue(datas.size() == 5);
    }

    /**
     * list 的 add 方法测试
     */
    @Test
    public void listAddTest() {
        String test = "jungle68";
        List<String> datas = new ArrayList<>();
        datas.add(0, test);
        for (int i = 0; i < 5; i++) {
            datas.add("test   " + i);
        }
        datas.add(0, test);
        Assert.assertTrue(datas.size() == 7);
    }

    @Test
    public void object2Map() {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setName("jungle68");
        Map<String, Object> optionsMap = DataDealUitls.transBean2Map(userInfoBean);
        for (Map.Entry<String, Object> entry : optionsMap.entrySet()) {
            LogUtils.d(TAG, "Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Test
    public void jsonObject2map() {
        String jsonstr = "{\"token\":\"l6NOIWOwcwEzENBQWkb23s57MVmvjNLPHN4D7I5X:rP3G9ZXRk6MjhnXY2vpVKmxWOUM\\u003d:eyJyZXR1cm5Cb2R5Ijoie1wicmVzb3VyY2VcIjogJCh4OnJlc291cmNlKX0iLCJzY29wZSI6InRzcGx1czoyMDE3XC8wNFwvMjhcLzA4MThcLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZyIsImRlYWRsaW5lIjoxNDkzNDUyOTk4LCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC16Mi5xaW5pdS5jb20iLCItSCB1cC16Mi5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjYwLjIxNC4xOTgiXX0\\u003d\",\"key\":\"2017/04/28/0818/9756FCCF72E47A2FBA935AE9213EB1E8.jpg\",\"x:resource\":\"MjAxNy8wNC8yOC8wODE4Lzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZw\\u003d\\u003d\"}";

        Map<String, Object> retMap = new Gson().fromJson(jsonstr,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        for (Map.Entry<String, Object> entry : retMap.entrySet()) {
            System.out.println( "Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        String jsonstr2 = "{\n" +
                "    \"phone\": [\n" +
                "        \"手机号码错误\"\n" +
                "    ],\n" +
                "    \"password\": [\n" +
                "        \"用户密码错误\"\n" +
                "    ]\n" +
                "}";

        Map<String, String[]> retMap2 = new Gson().fromJson(jsonstr2,
                new TypeToken<Map<String, String[]>>() {
                }.getType());
        for (Map.Entry<String, String[]> entry : retMap2.entrySet()) {
            System.out.println("Key2 = " + entry.getKey() + ", Value2 = " + entry.getValue()[0]);
        }


    }

    @Test
    public void objecttst() {
        Object object = "{\n" +
                "            \"token\":\"l6NOIWOwcwEzENBQWkb23s57MVmvjNLPHN4D7I5X:mrP5oxdTmdSqDGAQs8ZhtIZdCBY=:eyJyZXR1cm5Cb2R5Ijoie1wicmVzb3VyY2VcIjogJCh4OnJlc291cmNlKX0iLCJzY29wZSI6InRzcGx1czoyMDE3XC8wNFwvMjhcLzA3MDFcLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZyIsImRlYWRsaW5lIjoxNDkzNDUxNjAxLCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC16Mi5xaW5pdS5jb20iLCItSCB1cC16Mi5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjYwLjIxNC4xOTgiXX0=\",\n" +
                "            \"key\":\"2017/04/28/0701/9756FCCF72E47A2FBA935AE9213EB1E8.jpg\",\n" +
                "            \"x:resource\":\"MjAxNy8wNC8yOC8wNzAxLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZw==\"\n" +
                "        }";
        LogUtils.d(TAG, "object = " + object.toString());
    }

}

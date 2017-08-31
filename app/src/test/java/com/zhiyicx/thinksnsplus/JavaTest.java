package com.zhiyicx.thinksnsplus;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.imsdk.entity.ChatRoom;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.emory.mathcs.backport.java.util.Arrays;
import rx.Observable;
import rx.Subscriber;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter.DEFAULT_LOADING_SHOW_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */
@SuppressLint("LogNotUsed")
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

    @Test
    public void testTime() {
        String time = "2017-06-15 02:15:25";
        System.out.println("result::" + TimeUtils.getTimeFriendlyForDetail(time));
        System.out.println("result1::" + TimeUtils.getTimeFriendlyNormal(time));
        System.out.println("result2::" + TimeUtils.utc2LocalStr(time));
        System.out.println("result3::" + TimeUtils.getifferenceDays(TimeUtils.utc2LocalLong(time)));
    }

    @Test
    public void matchTest() {
        String reg = "(@!\\[.*]\\((\\d+)\\))";
        String test = "xxx@![image](123)ssss@![image](123)";
        Matcher matcher = Pattern.compile(reg).matcher(test);
        if (matcher.find()) {
            System.out.println("result::" + matcher.group(0));
            System.out.println("result::" + matcher.group(1));
            System.out.println("result::" + matcher.group(2));
            System.out.println("result::" + matcher.groupCount());

        }
    }

    @Test
    public void doubleTest() {
        double d = 5.0;
        System.out.println("result::" + PayConfig.realCurrencyFen2Yuan(d));
    }

    @Test
    public void bigDoubleTest() {
        double d = 11111111111111111111d;
        BigDecimal totalAmount = new BigDecimal(d);
        new BigDecimal(new Double(totalAmount.doubleValue()).toString());
        NumberFormat format = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        format.setGroupingUsed(false);

        format.format(d).toString();

        System.out.println("result1::" + totalAmount);
        System.out.println("result2::" + totalAmount.toString());
        System.out.println("result3::" + d);
    }

    @Test
    public void matchTest2() {
        String reg = "¥\\d+\\.\\d+";
        String test = " 2打赏 · 6 评论 ¥3.5啦啦啦";
        Matcher matcher = Pattern.compile(reg).matcher(test);
        if (matcher.find()) {
            System.out.println("result::" + matcher.group(0));
            System.out.println("result2::" + matcher.groupCount());

        }
    }

    @Test
    public void replaceTest() {
        String tag = "@![image](580)哈哈哈哈哈ヽ(ｏ`皿′ｏ)ﾉ((*゜Д゜)ゞ”😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁@![image](581)";
        Pattern pattern = Pattern.compile("@!\\[.*?]\\((\\d+)\\)");
        System.out.print(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, "result:: " + pattern.matcher(tag).replaceAll("")));
    }

    @Test
    public void subTest() {
        String reg = "@!\\[.*]\\((\\d+)\\)";
        String targetStr = "xxx@![image](123)ssss@![image](456)";
        Pattern pattern = Pattern.compile("@!\\[.*?]\\((\\d+)\\)");
        Matcher matcher1 = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher1.find()) {

            String imageMarkDown = matcher1.group(0);
            String id = matcher1.group(1);

            String imgPath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            String iamgeTag = imageMarkDown.replaceAll("\\d+", imgPath).replace("@", "");
            targetStr = targetStr.replace(imageMarkDown, iamgeTag);

            System.out.println("targetStr result:: " + targetStr);

            System.out.println("result:: " + matcher1.group(1));
            if (matcher1.start() > lastIndex) {
                System.out.println("result 1 :: " + targetStr.substring(lastIndex, matcher1.start()));
            }
            String result2 = targetStr.substring(matcher1.start(), matcher1.end());
            Matcher matcher2 = Pattern.compile(reg).matcher(result2);
            System.out.println("result 2 :: " + result2);

            if (matcher2.find()) {
                System.out.println("matcher 2 :: " + matcher2.group(0));
                System.out.println("matcher 2 :: " + matcher2.group(1));
                System.out.println("matcher 2 :: " + matcher2.group(0).replaceAll("\\d+","tym").replaceAll("@",""));
            }
            lastIndex = matcher1.end();
        }
        if (lastIndex != targetStr.length()) {
            System.out.println("result 3 :: " + targetStr.substring(lastIndex, targetStr.length()));
        }

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

    @SuppressLint("LogNotUsed")
    @Test
    public void jsonObject2map() {
//        String jsonstr = "{\"token\":\"l6NOIWOwcwEzENBQWkb23s57MVmvjNLPHN4D7I5X:rP3G9ZXRk6MjhnXY2vpVKmxWOUM\\u003d:eyJyZXR1cm5Cb2R5Ijoie1wicmVzb3VyY2VcIjogJCh4OnJlc291cmNlKX0iLCJzY29wZSI6InRzcGx1czoyMDE3XC8wNFwvMjhcLzA4MThcLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZyIsImRlYWRsaW5lIjoxNDkzNDUyOTk4LCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC16Mi5xaW5pdS5jb20iLCItSCB1cC16Mi5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjYwLjIxNC4xOTgiXX0\\u003d\",\"key\":\"2017/04/28/0818/9756FCCF72E47A2FBA935AE9213EB1E8.jpg\",\"x:resource\":\"MjAxNy8wNC8yOC8wODE4Lzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZw\\u003d\\u003d\"}";
//
//        Map<String, Object> retMap = new Gson().fromJson(jsonstr,
//                new TypeToken<Map<String, Object>>() {
//                }.getType());
//        for (Map.Entry<String, Object> entry : retMap.entrySet()) {
//            System.out.println( "Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
//        String jsonstr2 = "{\n" +
//                "    \"phone\": [\n" +
//                "        \"手机号码错误\"\n" +
//                "    ],\n" +
//                "    \"password\": [\n" +
//                "        \"用户密码错误\"\n" +
//                "    ]\n" +
//                "}";
//
//        Map<String, String[]> retMap2 = new Gson().fromJson(jsonstr2,
//                new TypeToken<Map<String, String[]>>() {
//                }.getType());
//        for (Map.Entry<String, String[]> entry : retMap2.entrySet()) {
//            System.out.println("Key2 = " + entry.getKey() + ", Value2 = " + entry.getValue()[0]);
//        }
        String test = "{\n" +
                "    \"im:serve\": \"127.0.0.1:9900\", \n" +
                "    \"im:helper\": [ \n" +
                "        {\n" +
                "            \"uid\": \"1\", \n" +
                "            \"url\": \"https://plus.io/users/1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        System.out.println("---- = " + new Gson().fromJson(test, SystemConfigBean.class).toString());

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

    @Test
    public void rxZipTest() {
        List<Observable<String>> datas = new ArrayList<>();
//        datas.add(Observable.just("123"));
//        datas.add(Observable.just("456"));
//        datas.add(Observable.just("789"));

        Observable.zip(datas, new FuncN<String>() {
            @Override
            public String call(Object... args) {
                for (Object arg : args) {
                    System.out.println("args = " + arg.toString());
                }
                System.out.println("args = -----------------------");
                return "12123";
            }
        })
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        System.out.println(" hello world : " + data);
                    }
                })
        ;
    }


    @Test
    public void rxEmptyTest() {
        Observable.empty()
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Sys -----1-------= " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Sys = -----2-------" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("o = -----1-------" + o);
                        System.out.println("Sys = -----3-------" + Thread.currentThread().getName());
                    }
                });
        System.out.println("Sys = " + Thread.currentThread().getName());
    }

    @Test
    public void rxTimerTest() {
        Observable.timer(DEFAULT_LOADING_SHOW_TIME, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("aLong = onCompleted ");
                        Assert.assertTrue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Assert.assertFalse(false);
                        System.out.println("aLong = onError ");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Assert.assertTrue(aLong == DEFAULT_LOADING_SHOW_TIME);
                        System.out.println("aLong = " + aLong);
                    }
                });
    }

    @Test
    public void tryCatchTest() {
        String[] a = {"123", "4569"};
        String b = null;
        String c = null;
        String d = null;
        try {
            b = a[0].toString();
            c = a[1].toString();
            d = a[5].toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(a[1].equals("4569"));
        Assert.assertTrue(b.equals("123"));
        Assert.assertTrue(c.equals("4569"));
        Assert.assertTrue(d == null);
    }

    /**
     * 地区解析测试
     */
    @Test
    public void locationParseTest() {
        String data = "[{\"items\":[{\"id\":3,\"name\":\"市辖区\",\"pid\":2,\"extends\":\"\",\"created_at\":\"2017-04-28 07:49:48\",\"updated_at\":\"2017-04-28 07:49:48\"},{\"id\":18,\"name\":\"县\",\"pid\":2,\"extends\":\"\",\"created_at\":\"2017-04-28 07:49:49\",\"updated_at\":\"2017-04-28 07:49:49\"}],\"tree\":{\"id\":2,\"name\":\"北京市\",\"pid\":1,\"extends\":\"\",\"created_at\":\"2017-04-28 07:49:48\",\"updated_at\":\"2017-04-28 07:49:48\",\"parent\":{\"id\":1,\"name\":\"中国\",\"pid\":0,\"extends\":\"3\",\"created_at\":\"2017-04-28 07:49:48\",\"updated_at\":\"2017-04-28 07:49:48\",\"parent\":null}}}]";
        List<LocationContainerBean> lodAta = new Gson().fromJson(data, new TypeToken<List<LocationContainerBean>>() {
        }.getType());
        List<LocationBean> result = new ArrayList<>();

        for (LocationContainerBean locationContainerBean : lodAta) {
            if (locationContainerBean.getItems() == null || locationContainerBean.getItems().isEmpty()) {
                result.add(locationContainerBean.getTree());
            } else {
                for (LocationBean locationBean : locationContainerBean.getItems()) {
                    locationBean.setParent(locationContainerBean.getTree());
                    result.add(locationBean);
                }
            }
        }
        Assert.assertTrue(LocationBean.getlocation(result.get(0)).equals("中国，北京市，市辖区"));
        Assert.assertTrue(LocationBean.getlocation(result.get(1)).equals("中国，北京市，县"));

    }


    @Test
    public void listRemoveDuplicateTest() {

        List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(1);
        data.add(4);
        data.add(2);
        data.add(5);
        data.add(6);
        data.add(1);
        data.add(6);

        ConvertUtils.removeDuplicate(data);
        Assert.assertTrue(data.size() == 6);
        System.out.println("data = " + data.subList(0, data.size()));


    }

    @Test
    public void listAddAllTest() {
//        List<List<AuthBean>> data = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            data.add(new AuthBean(i));
//        }
//        List<List<AuthBean>> test1 = new ArrayList<>();
//        test1.addAll(data);
//
//        List<List<AuthBean>> test2 = new ArrayList<>();
//        test2.addAll(data);
//
//        test2.subList(0, 5);
//
//        System.out.println("test1 = " + test1.size());
//        System.out.println("test2 = " + test2.size());

    }

    @Test
    public void testEmoji() {
        containsEmoji("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁22");
    }

    private static boolean containsEmoji(String str) {
        int test = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                test++;
            }
        }
        System.out.println("result::" + test);
        System.out.println("len::" + len);
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !(codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD);
    }
}

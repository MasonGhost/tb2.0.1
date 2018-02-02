package com.zhiyicx.thinksnsplus;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import static com.zhiyicx.common.utils.ConvertUtils.praseErrorMessage;
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
    public void testLongImage() {
        float netHeight, netWidth;
        int sw, sh;
        sw = 720;
        sh = 1080;
        netWidth = 900;
        netHeight = 800;
        isLongImage(netHeight, netWidth, sw, sh);

    }

    public void isLongImage(float netHeight, float netWidth, int sw, int sh) {
        float net = netHeight / netWidth;
        float result = 0;
        if (net >= 3 || net <= .3f) {

            result = sw / netWidth;

            if (result <= .3f) {

            } else {
                result = result * netHeight / sh;
            }
        }
        boolean isLong = (result >= 3 || result <= .3f) && result > 0;
        System.out.println("result:::" + result + "");
        System.out.println("isLongImage:::" + isLong + "");
        if (!isLong) {
            netWidth += 20;
            isLongImage(netHeight, netWidth, sw, sh);
        }
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
    public void testCatchGroup() {
        String src = "<span style=\"font-family: sans-serif;\">好刚刚给</span><div style=\"font-family: sans-serif;\">黄河鬼棺给</div><h1 style=\"font-family: sans-serif;\">好好奋斗是</h1><div style=\"font-family: sans-serif;\"><b><i>v刚发的的</i></b></div><div style=\"font-family: sans-serif;\"><b><i><strike>广告费多大的</strike></i></b></div><hr style=\"color: rgb(0, 0, 0);\"><div style=\"font-family: sans-serif;\"><br></div><h1 style=\"font-family: sans-serif;\">复方丹参</h1><div><br></div><div class=\"block\" contenteditable=\"false\">\n" +
                "                                                                  \t\t\t\t<div class=\"img-block\">\n" +
                "                                                                  \t\t\t\t\n" +
                "                                                                  \t\t\t\t\n" +
                "                                                                  \t\t\t\t<div class=\"delete\">\n" +
                "                                                                  \t\t\t\t\t\n" +
                "                                                                  \t\t\t\t\t<div class=\"tips\">图片上传失败，请点击重试</div>\n" +
                "                                                                  \t\t\t\t\t<div class=\"markdown\">@![image](6104)</div>\n" +
                "                                                                  \t\t\t\t</div></div>\n" +
                "                                                                  \t\t\t\t\n" +
                "                                                                  \t\t\t</div><h1><br></h1></div>";
        String reg = MarkdownConfig.TEST_HTML_FORMAT;
        Matcher matcher = Pattern.compile(reg).matcher(src);
        String result = "";

        while (matcher.find()) {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println("reg::" + i + ":::" + matcher.group(i));
            }
            String group2 = matcher.group(2);
            String group3 = matcher.group(3);
            if (TextUtils.isEmpty(group2)) {
                result = src.replace(group2, "p");
            }
            if (TextUtils.isEmpty(group3)) {
                result = result.replace(group3, "p");
            }
        }
        System.out.println("result::" + result);
    }

    @Test
    public void testFilter() { // <(a|/a)(href="(.*)").*>
        String source = "<ahref=\"http://rde\"class=\"editor-link\">tgfff</a>@![image](6173)";
        Matcher matcher = Pattern.compile(MarkdownConfig.LINK_WORDS_FORMAT).matcher(source);
        while (matcher.find()) {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println("reg::" + i + ":::" + matcher.group(i));
            }
            source=source.replaceFirst(MarkdownConfig.LINK_WORDS_FORMAT,matcher.group(3));
        }
        System.out.println(source);
    }

    @Test
    public void testList2Map() {
        CreateCircleBean createCircleBean = new CreateCircleBean();
        CreateCircleBean.TagId tagId = new CreateCircleBean.TagId(2);
        CreateCircleBean.TagId tag = new CreateCircleBean.TagId(3);
        List<CreateCircleBean.TagId> tags = new ArrayList<>();
        tags.add(tag);
        tags.add(tagId);
        createCircleBean.setNotice("notice");
        createCircleBean.setTags(tags);
        System.out.print(DataDealUitls.transBean2MapWithArray(createCircleBean).toString());
    }

    @Test
    public void testArrays() {
        String[] test = new String[2];
        test[0] = "aa";
        test[1] = "bb";
        System.out.print(java.util.Arrays.toString(test).contains("aa"));
    }

    @Test
    public void testInt() {
        String test = "[对对对](57941)";//  \[(.*?)]\((.*?)\)
//        Matcher matcher = Pattern.compile("@!\\[.*?]\\((\\d+)\\)").matcher(test);

        Matcher matcher = null;
        try {
            matcher = Pattern.compile(MarkdownConfig.LINK_FORMAT).matcher(test);
            while (matcher.find()) {
                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

//        while (matcher.find()) {
//            int count = matcher.groupCount();
//            for (int i = 0; i < count; i++) {
//                System.out.println(matcher.group(i));
//            }
//        }
    }

    @Test
    public void timeTest() {
        String time = "2017-08-04 09:16:19";

        long timeMillisSpace = System.currentTimeMillis() - TimeUtils.utc2LocalLong(time);

        double daySpace = (timeMillisSpace / (double) (1000 * 60 * 60 * 24));

        long test = TimeUnit.DAYS.convert(timeMillisSpace, TimeUnit.MILLISECONDS);


        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        Date today = new Date(System.currentTimeMillis());

        Date otherDay = new Date(TimeUtils.utc2LocalLong(time));

        int now = Integer.parseInt(sdf.format(today));
        int other = Integer.parseInt(sdf.format(otherDay));

        int day = TimeUtils.getifferenceDays(System.currentTimeMillis() - TimeUtils.utc2LocalLong(time));
        String str = TimeUtils.getTimeFriendlyForDetail(time);

        if (daySpace > test && daySpace < 9) {
            System.out.println("result:daySpace:" + (now - other));
        } else {
            System.out.println("result:daySpace:" + daySpace);
        }


    }

    @Test
    public void testContain() {

//        String str = "1号线@![image]号漕宝路";
//        String reg = "[\\s\\S]*@!\\[\\S*][\\s\\S]*";
//        if (str.matches(reg)) {
//            System.out.println("result1::" + str);
//        }

        String text = "ggfdd@![image](2537)dddd@![tym](2538)";

        String reg1 = "@!(\\[(.*?)])\\(((\\d+))\\)";
        Matcher matcher = Pattern.compile(reg1).matcher(text);


        while (matcher.find()) {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println("reg1:::" + matcher.group(i));
            }
        }


//        if (text.matches("\\.*@!\\[.*?]\\((\\d+)\\)\\.*")) {
//            int id = RegexUtils.getImageId(text);
//            String imagePath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
//            System.out.println("result1:id:" + id);
//            System.out.println("result2:imagePath:" + imagePath);
//        }
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
    public void matchTest() {// @![image](2604)
        String reg = "(@!\\[.*]\\((\\d+)\\))";

        try {
            String sss = "@!\\[.*?]\\((\\d+)\\)";
            String res = "@![image](2604)@![image](2609)";
            Matcher matcher2 = Pattern.compile(sss).matcher(res);
//            String test=res.substring(0,matcher2.start());


            int lastIndex = 0;
            if (matcher2.find()) {
                System.out.println("result:test:" + matcher2.start());
                System.out.println("result:test:" + matcher2.end());
                System.out.println("result:result:" + res.substring(matcher2.start(), matcher2.end()));
                System.out.println("result:count:" + matcher2.groupCount());
                System.out.println("result:count:" + matcher2.group(1));


//                if (matcher2.start() > lastIndex) {
//                    String result1 = targetStr.substring(lastIndex, matcher1.start());// 文字
//                    splitTextList.add(result1);
//                }
//                String result2 = targetStr.substring(matcher1.start(), matcher1.end());// 图片
//                splitTextList.add(result2);
//
//                lastIndex = matcher1.end();
            }
//            if (matcher2.find()) {
//
//            }

        } catch (Exception e) {
            System.out.println("result::" + e.toString());
        }

        String test = "xxx@![image](123)ssss@![image](125)";
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
        System.out.println("result:11:" + String.valueOf(10 * 111111111));
    }

    @Test
    public void doubleBitMoney() {
        int test = 99999999;
        long money = 10 * test;

        System.out.println("result::" + money);
    }

    @Test
    public void singleImageTest() {
        String input = "@![image](2580),,,@![image](1520)";

        System.out.println("result::" + RegexUtils.getImageId(input));
    }

    @Test
    public void bigDoubleTest() {
        double d = 11111111111111111111d;
        BigDecimal totalAmount = new BigDecimal(d);
        new BigDecimal(Double.valueOf(totalAmount.doubleValue()).toString());
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
                System.out.println("matcher 2 :: " + matcher2.group(0).replaceAll("\\d+", "tym").replaceAll("@", ""));
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
        LogUtils.d(TAG, "ConvertUtils.removeSymbolStartWith(addBtnAnimation,\",\") = " + ConvertUtils.removeSymbolStartWith(test, ","));
    }

    /**
     * 去除wei部符号
     */
    @Test
    public void removeSymbolEndWith() {
        String test = ",,2,3,,";
        LogUtils.d(TAG, "ConvertUtils.removeSymbolStartWith(addBtnAnimation,\",\") = " + ConvertUtils.removeSymbolEndWith(test, ","));
    }

    /**
     * list 的 set 方法测试
     */
    @Test
    public void listSetTest() {
        String test = "jungle68";
        List<String> datas = new ArrayList<>();

//        datas.add(0, addBtnAnimation); 没有数据的时候set异常
        LogUtils.d(TAG, "datas = " + datas.toString());
        try {
            datas.set(0, test);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
        for (int i = 0; i < 5; i++) {
            datas.add("addBtnAnimation   " + i);
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
            datas.add("addBtnAnimation   " + i);
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
//        String jsonstr = "{\"token\":\"l6NOIWOwcwEzENBQWkb23s57MVmvjNLPHN4D7I5X:rP3G9ZXRk6MjhnXY2vpVKmxWOUM\\u003d
// :eyJyZXR1cm5Cb2R5Ijoie1wicmVzb3VyY2VcIjogJCh4OnJlc291cmNlKX0iLCJzY29wZSI6InRzcGx1czoyMDE3XC8wNFwvMjhcLzA4MThcLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZyIsImRlYWRsaW5lIjoxNDkzNDUyOTk4LCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC16Mi5xaW5pdS5jb20iLCItSCB1cC16Mi5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjYwLjIxNC4xOTgiXX0\\u003d\",\"key\":\"2017/04/28/0818/9756FCCF72E47A2FBA935AE9213EB1E8.jpg\",\"x:resource\":\"MjAxNy8wNC8yOC8wODE4Lzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZw\\u003d\\u003d\"}";
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
                "            \"token\":\"l6NOIWOwcwEzENBQWkb23s57MVmvjNLPHN4D7I5X:mrP5oxdTmdSqDGAQs8ZhtIZdCBY" +
                "=:eyJyZXR1cm5Cb2R5Ijoie1wicmVzb3VyY2VcIjogJCh4OnJlc291cmNlKX0iLCJzY29wZSI6InRzcGx1czoyMDE3XC8wNFwvMjhcLzA3MDFcLzk3NTZGQ0NGNzJFNDdBMkZCQTkzNUFFOTIxM0VCMUU4LmpwZyIsImRlYWRsaW5lIjoxNDkzNDUxNjAxLCJ1cEhvc3RzIjpbImh0dHA6XC9cL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6XC9cL3VwbG9hZC16Mi5xaW5pdS5jb20iLCItSCB1cC16Mi5xaW5pdS5jb20gaHR0cDpcL1wvMTgzLjYwLjIxNC4xOTgiXX0=\",\n" +
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
        String data = "[{\"items\":[{\"id\":3,\"name\":\"市辖区\",\"pid\":2,\"extends\":\"\",\"created_at\":\"2017-04-28 07:49:48\"," +
                "\"updated_at\":\"2017-04-28 07:49:48\"},{\"id\":18,\"name\":\"县\",\"pid\":2,\"extends\":\"\",\"created_at\":\"2017-04-28 " +
                "07:49:49\",\"updated_at\":\"2017-04-28 07:49:49\"}],\"tree\":{\"id\":2,\"name\":\"北京市\",\"pid\":1,\"extends\":\"\"," +
                "\"created_at\":\"2017-04-28 07:49:48\",\"updated_at\":\"2017-04-28 07:49:48\",\"parent\":{\"id\":1,\"name\":\"中国\",\"pid\":0," +
                "\"extends\":\"3\",\"created_at\":\"2017-04-28 07:49:48\",\"updated_at\":\"2017-04-28 07:49:48\",\"parent\":null}}}]";
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
        return !(codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF ||
                codePoint >= 0xE000 && codePoint <= 0xFFFD);
    }


    /**
     * 测试 String 的 container
     */
    @Test
    public void testStringContainer() {
        String user_ids = "14,12,9,88,33";
        String user_ids2 = "12,9,88,33";
        String user_ids3 = "12";
        String user_ids4 = "14,12,9,88,12";
        String user_ids5 = "14,12,9,12,33";
        String user_ids6 = "12,12,9,12,33";
        String currentUserId = "12";


        checkUser(user_ids, currentUserId);
        checkUser(user_ids2, currentUserId);
        checkUser(user_ids3, currentUserId);
        checkUser(user_ids4, currentUserId);
        checkUser(user_ids5, currentUserId);
        checkUser(user_ids6, currentUserId);


    }

    private void checkUser(String user_ids, String currentUserId) {
        String[] users = user_ids.split(ConstantConfig.SPLIT_SMBOL);
        boolean hasCurrentUser = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (String user : users) {
            if (currentUserId.equals(user)) {
                hasCurrentUser = true;
            } else {
                stringBuilder.append(user);
                stringBuilder.append(ConstantConfig.SPLIT_SMBOL);
            }
        }
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        user_ids = stringBuilder.toString();

        System.out.println(user_ids);

        Assert.assertTrue(hasCurrentUser);
    }

    /**
     * 测试网络错误数据
     */
    @Test
    public void testNetErrorResponseMessage() {
        String response1 = "{ \"message\": \"this is a message.\" }";
        String response2 = "{ \"message\": [ \"This is amessage array item.\" ] }";
        String response3 = "{\n" +
                "    \"key\": [ \"value\" ],\n" +
                "    \"key2\": [ \"value\", \"value2\" ]\n" +
                "}";
        String response4 = "{\n" +
                "    \"message\": \"This is a message\",\n" +
                "    \"errors\": {\n" +
                "        \"key1\": [ \"value1\" ],\n" +
                "        \"key2\": [ \"value1\", \"value2\" ]\n" +
                "    }\n" +
                "}";
        String response5 = "{\n" +
                "    \"message\": \"请绑定账号\",\n" +
                "    \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Application.php\",\n" +
                "    \"line\": 926,\n" +
                "    \"trace\": [\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/helpers.php\",\n" +
                "            \"line\": 34,\n" +
                "            \"function\": \"abort\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Application\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                404,\n" +
                "                \"请绑定账号\",\n" +
                "                []\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/resources/repositorie/sources/plus-socialite/src/Traits/SocialiteDriverHelper" +
                ".php\",\n" +
                "            \"line\": 137,\n" +
                "            \"function\": \"abort\",\n" +
                "            \"args\": [\n" +
                "                404,\n" +
                "                \"请绑定账号\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/resources/repositorie/sources/plus-socialite/src/Drivers/DriverAbstract.php\"," +
                "\n" +
                "            \"line\": 44,\n" +
                "            \"function\": \"SlimKit\\\\PlusSocialite\\\\Traits\\\\{closure}\",\n" +
                "            \"class\": \"SlimKit\\\\PlusSocialite\\\\Drivers\\\\DriverAbstract\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                404,\n" +
                "                \"请绑定账号\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/resources/repositorie/sources/plus-socialite/src/Traits/SocialiteDriverHelper" +
                ".php\",\n" +
                "            \"line\": 138,\n" +
                "            \"function\": \"SlimKit\\\\PlusSocialite\\\\Drivers\\\\{closure}\",\n" +
                "            \"class\": \"SlimKit\\\\PlusSocialite\\\\Drivers\\\\DriverAbstract\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/resources/repositorie/sources/plus-socialite/src/Drivers/DriverAbstract.php\"," +
                "\n" +
                "            \"line\": 45,\n" +
                "            \"function\": \"abortIf\",\n" +
                "            \"class\": \"SlimKit\\\\PlusSocialite\\\\Drivers\\\\DriverAbstract\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                true,\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/resources/repositorie/sources/plus-socialite/src/API/Controllers" +
                "/SocialiteController.php\",\n" +
                "            \"line\": 71,\n" +
                "            \"function\": \"authUser\",\n" +
                "            \"class\": \"SlimKit\\\\PlusSocialite\\\\Drivers\\\\DriverAbstract\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                \"D4AA7C164E58457F9B8D20E3F594A653\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/ControllerDispatcher.php\",\n" +
                "            \"line\": 48,\n" +
                "            \"function\": \"checkAuth\",\n" +
                "            \"class\": \"SlimKit\\\\PlusSocialite\\\\API\\\\Controllers\\\\SocialiteController\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                \"QQ\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Route.php\",\n" +
                "            \"line\": 205,\n" +
                "            \"function\": \"dispatch\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\ControllerDispatcher\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"uri\": \"api/v2/socialite/{provider}\",\n" +
                "                    \"methods\": [\n" +
                "                        \"POST\"\n" +
                "                    ],\n" +
                "                    \"action\": {\n" +
                "                        \"middleware\": [\n" +
                "                            \"api\"\n" +
                "                        ],\n" +
                "                        \"uses\": \"SlimKit\\\\PlusSocialite\\\\API\\\\Controllers\\\\SocialiteController@checkAuth\",\n" +
                "                        \"controller\": \"SlimKit\\\\PlusSocialite\\\\API\\\\Controllers\\\\SocialiteController@checkAuth\",\n" +
                "                        \"namespace\": null,\n" +
                "                        \"prefix\": \"api/v2/socialite\",\n" +
                "                        \"where\": []\n" +
                "                    },\n" +
                "                    \"controller\": {},\n" +
                "                    \"defaults\": [],\n" +
                "                    \"wheres\": [],\n" +
                "                    \"parameters\": {\n" +
                "                        \"provider\": \"qq\"\n" +
                "                    },\n" +
                "                    \"parameterNames\": [\n" +
                "                        \"provider\"\n" +
                "                    ],\n" +
                "                    \"computedMiddleware\": [\n" +
                "                        \"api\"\n" +
                "                    ],\n" +
                "                    \"compiled\": {}\n" +
                "                },\n" +
                "                {},\n" +
                "                \"checkAuth\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Route.php\",\n" +
                "            \"line\": 162,\n" +
                "            \"function\": \"runController\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Route\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Router.php\",\n" +
                "            \"line\": 610,\n" +
                "            \"function\": \"run\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Route\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 30,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Router\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Middleware/SubstituteBindings" +
                ".php\",\n" +
                "            \"line\": 41,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Middleware\\\\SubstituteBindings\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Middleware/ThrottleRequests" +
                ".php\",\n" +
                "            \"line\": 57,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Middleware\\\\ThrottleRequests\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {},\n" +
                "                600,\n" +
                "                \"1\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 102,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Router.php\",\n" +
                "            \"line\": 612,\n" +
                "            \"function\": \"then\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Router.php\",\n" +
                "            \"line\": 571,\n" +
                "            \"function\": \"runRouteWithinStack\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Router\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"uri\": \"api/v2/socialite/{provider}\",\n" +
                "                    \"methods\": [\n" +
                "                        \"POST\"\n" +
                "                    ],\n" +
                "                    \"action\": {\n" +
                "                        \"middleware\": [\n" +
                "                            \"api\"\n" +
                "                        ],\n" +
                "                        \"uses\": \"SlimKit\\\\PlusSocialite\\\\API\\\\Controllers\\\\SocialiteController@checkAuth\",\n" +
                "                        \"controller\": \"SlimKit\\\\PlusSocialite\\\\API\\\\Controllers\\\\SocialiteController@checkAuth\",\n" +
                "                        \"namespace\": null,\n" +
                "                        \"prefix\": \"api/v2/socialite\",\n" +
                "                        \"where\": []\n" +
                "                    },\n" +
                "                    \"controller\": {},\n" +
                "                    \"defaults\": [],\n" +
                "                    \"wheres\": [],\n" +
                "                    \"parameters\": {\n" +
                "                        \"provider\": \"qq\"\n" +
                "                    },\n" +
                "                    \"parameterNames\": [\n" +
                "                        \"provider\"\n" +
                "                    ],\n" +
                "                    \"computedMiddleware\": [\n" +
                "                        \"api\"\n" +
                "                    ],\n" +
                "                    \"compiled\": {}\n" +
                "                },\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Router.php\",\n" +
                "            \"line\": 549,\n" +
                "            \"function\": \"dispatchToRoute\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Router\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Kernel.php\",\n" +
                "            \"line\": 176,\n" +
                "            \"function\": \"dispatch\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Router\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 30,\n" +
                "            \"function\": \"Illuminate\\\\Foundation\\\\Http\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Kernel\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/fideloper/proxy/src/TrustProxies.php\",\n" +
                "            \"line\": 56,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Fideloper\\\\Proxy\\\\TrustProxies\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Middleware" +
                "/TransformsRequest.php\",\n" +
                "            \"line\": 30,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Middleware\\\\TransformsRequest\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Middleware" +
                "/TransformsRequest.php\",\n" +
                "            \"line\": 30,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Middleware\\\\TransformsRequest\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Middleware" +
                "/ValidatePostSize.php\",\n" +
                "            \"line\": 27,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Middleware\\\\ValidatePostSize\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Middleware" +
                "/CheckForMaintenanceMode.php\",\n" +
                "            \"line\": 46,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 149,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Middleware\\\\CheckForMaintenanceMode\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                },\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Routing/Pipeline.php\",\n" +
                "            \"line\": 53,\n" +
                "            \"function\": \"Illuminate\\\\Pipeline\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Pipeline/Pipeline.php\",\n" +
                "            \"line\": 102,\n" +
                "            \"function\": \"Illuminate\\\\Routing\\\\{closure}\",\n" +
                "            \"class\": \"Illuminate\\\\Routing\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Kernel.php\",\n" +
                "            \"line\": 151,\n" +
                "            \"function\": \"then\",\n" +
                "            \"class\": \"Illuminate\\\\Pipeline\\\\Pipeline\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/vendor/laravel/framework/src/Illuminate/Foundation/Http/Kernel.php\",\n" +
                "            \"line\": 116,\n" +
                "            \"function\": \"sendRequestThroughRouter\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Kernel\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"file\": \"/home/wwwroot/thinksns-plus/public/index.php\",\n" +
                "            \"line\": 48,\n" +
                "            \"function\": \"handle\",\n" +
                "            \"class\": \"Illuminate\\\\Foundation\\\\Http\\\\Kernel\",\n" +
                "            \"type\": \"->\",\n" +
                "            \"args\": [\n" +
                "                {\n" +
                "                    \"attributes\": {},\n" +
                "                    \"request\": {},\n" +
                "                    \"query\": {},\n" +
                "                    \"server\": {},\n" +
                "                    \"files\": {},\n" +
                "                    \"cookies\": {},\n" +
                "                    \"headers\": {}\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Assert.assertTrue("this is a message.".equals(praseErrorMessage(response1)));
        Assert.assertTrue("This is amessage array item.".equals(praseErrorMessage(response2)));
        Assert.assertTrue("value".equals(praseErrorMessage(response3)));
        Assert.assertTrue("value1".equals(praseErrorMessage(response4)));
        praseErrorMessage(response5);

    }

    @Test
    public void testIMData() {
        JpushMessageBean jpushMessageBean;
        String response1 = "{\"seq\":1,\"msg_type\":0,\"cid\":461,\"mid\":445579829106966533,\"type\":\"im\",\"uid\":270}";
        jpushMessageBean = new Gson().fromJson(response1, JpushMessageBean.class);
        System.out.println("jpushMessageBean = " + jpushMessageBean);
    }

    @Test
    public void testCollectionSingle() {

        String[] split = new String[10000];
        for (int i = 0; i < 10000; i++) {
            split[i] = i + "";
        }

        AbstractList<String> mContentList = new ArrayList<>();

        long a = System.currentTimeMillis();
        for (String str : split) {
            mContentList.add(str);
        }
        System.out.println("System.currentTimeMillis() = " + (System.currentTimeMillis() - a));

        mContentList.clear();
        a = System.currentTimeMillis();
        Collections.addAll(mContentList, split);
        System.out.println("System.currentTimeMillis() = " + (System.currentTimeMillis() - a));


    }


    @Test
    public void testTimestrEques() {
        String s1 = "2003-12-12   11:30:24";
        String s2 = "2004-04-01   13:31:40";
        int res = s1.compareTo(s2);
        System.out.println("res = " + res);
        Assert.assertTrue(res < 0);
    }


    /**
     * 测试 List 删除
     */
    @Test
    public void testListRemove() {

        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(i);
        }
//  错误的
//        int size=datas.size();
//        for (int i = 0; i < size; i++) {
//            if (datas.get(i) % 3 == 0) {
//                datas.remove(datas.get(i));
//            }
//        }
//

        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i) % 3 == 0) {
                datas.remove(datas.get(i));
            }
        }
        for (Integer data : datas) {
            System.out.println("data = " + data);
        }
//        错误的
//        for (Integer data : datas) {
//            if(data%3==0){
//                datas.remove(data);
//            }
//        }
    }

    @Test
    public void testRomand() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println("random = " + random.nextInt(5) % (5));
        }
    }
}

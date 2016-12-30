package com.zhiyicx.imsdk.utils;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jungle on 16/8/12.
 * com.zhiyicx.imsdk.utils
 * zhibo_android
 * email:335891510@qq.com
 */
public class MessageHelperTest {

    @Before
    public void setUp() throws Exception {
    }

    /**
     * 解析返回的数据
     * @throws Exception
     */
    @Test
    public void testGetRecievedBody() throws Exception {
        String input_notsoupport = "T['msg',{'data':'nihao']";
        String resut = "['msg',{'data':'nihao']";
        Assert.assertEquals(resut, MessageHelper.getRecievedBody(input_notsoupport.getBytes()));
        System.out.println("111111-------"+MessageHelper.getRecievedBody(input_notsoupport.getBytes()));
        String input_zlib = "54['msg',{'data':'nihao']";
        Assert.assertNotEquals(resut, MessageHelper.getRecievedBody(ZipHelper.compressForZlib(input_zlib)));
        System.out.println("2222-------"+MessageHelper.getRecievedBody(ZipHelper.compressForZlib(input_zlib)));
        String input_gizp = "53['msg',{'data':'nihao']";
        Assert.assertNotEquals(resut, MessageHelper.getRecievedBody(ZipHelper.compressForGzip(input_zlib)));
        System.out.println("3333-------"+MessageHelper.getRecievedBody(ZipHelper.compressForGzip(input_zlib)));
    }

    @Test
    public void testBlooleanEques(){
        boolean a=true;
        boolean b=true;
        boolean c=false;
        Assert.assertEquals(true,b==a);
        Assert.assertEquals(false,b==c);

    }




}
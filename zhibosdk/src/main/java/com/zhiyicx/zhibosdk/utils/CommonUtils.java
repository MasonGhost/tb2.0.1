package com.zhiyicx.zhibosdk.utils;

import java.security.MessageDigest;

/**
 * Created by jungle on 16/7/12.
 * com.zhiyicx.zhibosdk.utils
 * zhibo_android
 * email:335891510@qq.com
 */
public class CommonUtils {


    /**
     * MD5
     *
     * @param string
     * @return
     *
     * @throws Exception
     */
    public static String MD5encode(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取限定范围的随机数
     * @param start
     * @param end
     * @return
     */
    public static int getRandom(int start, int end) {
        // 按照之前获取1-100随机数的方法
        // int number = (int) (Math.random() * 100) + 1;
        // int number = (int) (Math.random() * end) + start;
        // 发现有问题了，怎么办呢?
        int number = (int) (Math.random() * (end - start + 1)) + start;//可以自行记住（end-start+1)就是获取任意随机数的范围
        return number;
    }

}

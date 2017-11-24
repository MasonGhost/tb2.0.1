package com.zhiyicx.zhibosdk.utils;

import com.zhiyicx.imsdk.utils.ZipHelper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;

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

    /**
     * 获取网络返回数据体内容
     *
     * @param response 返回体
     * @return
     */
    public static String getResponseBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.errorBody();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        //获取content的压缩类型
        String encoding = response
                .headers()
                .get("Content-Encoding");
        Buffer clone = buffer.clone();
        return praseBodyString(responseBody, encoding, clone);
    }

    /**
     * 解析返回体数据内容
     *
     * @param responseBody 返回体
     * @param encoding     编码
     * @param clone        数据
     * @return
     */
    public static String praseBodyString(ResponseBody responseBody, String encoding, Buffer clone) {
        String bodyString;//解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            bodyString = ZipHelper.decompressForGzip(clone.readByteArray());//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());//解压
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
        return bodyString;
    }

}

package com.zhiyicx.zhibosdk.model.api;

import android.support.annotation.NonNull;

import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static android.content.ContentValues.TAG;


/**
 * Created by jess on 7/1/16.
 */
public class RequestIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
            LogUtils.errroInfo("request.body() == null");
        }

        //打印url信息
        String logUrl = request.url() + "";
        String method = request.method();
        logUrl = URLDecoder.decode(logUrl, "utf-8");
        try {
            LogUtils.debugInfo(String.format("Sending " + method + " Request %s on %n formdata --->  %s%n Connection ---> %s%n Headers ---> %s",
                    logUrl
                    , request.body() != null ? parseParams(request.body(), requestbuffer) : "null"
                    , chain.connection()
                    , request.headers()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Response originalResponse;
        Buffer buffer;
        Charset charset;
        try {
            originalResponse = chain.proceed(request);
            //打赢响应时间
            //读取服务器返回的结果
            ResponseBody responseBody = originalResponse.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            buffer = source.buffer();
            charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            long t1 = System.nanoTime();
            long t2 = System.nanoTime();
            //打印响应时间
            LogUtils.debugInfo(TAG, String.format("Received response code %d in %.1fms%n%s", originalResponse.code(), (t2 - t1) / 1e6d, originalResponse.headers()));


        } catch (IllegalStateException e) {
            // this method "throws IOException" anyway so we will not get a crash.
            throw new IOException(e);
        }
        return originalResponse;
    }

    @NonNull
    public static String parseParams(RequestBody body, Buffer requestbuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestbuffer.readUtf8(), "UTF-8");
        }
        return "multipart";
    }

}

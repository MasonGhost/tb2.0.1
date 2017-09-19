package com.zhiyicx.zhibosdk.model.api;

import com.zhiyicx.zhibosdk.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


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
        }
        else {
            LogUtils.errroInfo("request.body() == null");
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
        } catch (IllegalStateException e) {
            // this method "throws IOException" anyway so we will not get a crash.
            throw new IOException(e);
        }
        return originalResponse;
    }

    public String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }

}

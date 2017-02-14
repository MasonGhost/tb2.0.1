package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

import android.os.Handler;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class ProgressDataFetcher implements DataFetcher<InputStream> {
    private String photoUrl;
    private Call progressCall;
    private InputStream resultStream;
    private boolean isCancel;
    private Handler mHandler;

    public ProgressDataFetcher(String photoUrl, Handler handler) {
        this.photoUrl = photoUrl;
        mHandler = handler;
    }

    @Override
    public InputStream loadData(Priority priority) {
        // 重写Glide图片加载方法
        Request requst = new Request.Builder().url(photoUrl).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.interceptors().add(new ProgressIntercept());
        progressCall = okHttpClient.newCall(requst);
        try {
            Response response = progressCall.execute();
            if (isCancel) {
                return null;
            }
            if (!response.isSuccessful()) {
                throw new IOException("unexpected error" + response);
            }
            resultStream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStream;
    }

    @Override
    public void cleanup() {
        // 关闭相应资源
        if (resultStream != null) {
            try {
                resultStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return photoUrl;
    }

    @Override
    public void cancel() {
        isCancel = true;
    }

    private ProgressListener getProgressListener() {
        return null;
    }
}

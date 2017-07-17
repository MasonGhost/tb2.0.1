package com.zhiyicx.thinksnsplus.utils;

import android.content.Context;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UpdateInfoBean;

import org.lzh.framework.updatepluginlib.business.DownloadWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe 用来下载新的包
 * @date 2017/7/13
 * @contact email:648129313@qq.com
 */

public class DownLoadUtil extends DownloadWorker{

    private OkHttpClient mClient;
    private UpdateInfoBean mUpdateInfoBean; // 服务器请求到的信息
    private Context mContext;

    /**
     * 获取下载内容长度
     *
     * @param url 下载链接
     * @return length
     */
    private long getContentLength(String url) {
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long length = response.body().contentLength();
                response.close();
                return length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    protected void download(String url, File target) throws Exception {
// 在此进行文件下载任务。运行于子线程中。需要同步请求。
        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.get().build();
        Call call = mClient.newCall(request);
        target.delete();
        Response response = call.execute();
        int code = response.code();
        if (code < 200 || code >= 300) {
            // 通过抛出异常中断下载任务并提示用户下载失败
            throw new RuntimeException("下载失败");
        }
        long contentLength = response.body().contentLength();
        int bufLength = -1;
        InputStream inputStream = response.body().byteStream();
        OutputStream output = new FileOutputStream(target, false);
        byte[] buffer = new byte[8 * 1024];
        long start = System.currentTimeMillis();
        int offset = 0;
        while ((bufLength = inputStream.read(buffer)) != -1) {
            output.write(buffer,0,bufLength);
            output.flush();
            offset += bufLength;
            long end = System.currentTimeMillis();
            if (end - start > 1000) {
                // 发送下载进度信息。便于用于更新界面等操作
                sendUpdateProgress(offset,contentLength);
            }
        }
    }
}

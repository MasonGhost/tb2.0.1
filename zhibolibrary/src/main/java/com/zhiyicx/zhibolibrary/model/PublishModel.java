package com.zhiyicx.zhibolibrary.model;

import android.graphics.Bitmap;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public interface PublishModel {
    /**
     * 压缩bitmap到指定地址
     *
     * @param file
     * @param resizeBmp
     */
    boolean compressBitmap(File file, Bitmap resizeBmp);

    Observable<ResponseBody> downloadFile(String fileUrl);
}

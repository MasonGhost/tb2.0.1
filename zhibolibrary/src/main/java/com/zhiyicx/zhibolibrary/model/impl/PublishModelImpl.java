package com.zhiyicx.zhibolibrary.model.impl;

import android.graphics.Bitmap;

import com.zhiyicx.zhibolibrary.model.PublishModel;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.LiveService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public class PublishModelImpl implements PublishModel {
    private LiveService mService;
    private CommonService mCommonService;

    public PublishModelImpl(ServiceManager manager) {
        mService = manager.getLiveService();//初始化服务
        mCommonService=manager.getCommonService();
    }

    /**
     * 压缩bitmap到指定地址
     *
     * @param file
     * @param resizeBmp
     */
    @Override
    public boolean compressBitmap(File file, Bitmap resizeBmp) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }

    @Override
    public Observable<ResponseBody> downloadFile(String fileUrl) {
        return mCommonService.downloadFile(fileUrl);
    }
}

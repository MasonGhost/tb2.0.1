package com.zhiyicx.zhibosdk.model.imp;

import android.graphics.Bitmap;

import com.zhiyicx.zhibosdk.model.PublishModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.service.LiveService;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.entity.ZBApiJson;
import com.zhiyicx.zhibosdk.model.entity.ZBApiStream;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBCheckStreamPullJson;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBIconInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public class PublishModelImpl implements PublishModel {
    private LiveService mService;

    public PublishModelImpl(ZBServiceManager manager) {
        mService = manager.getLiveService();//初始化服务
    }

    @Override
    public Observable<ZBApiStream> createStream(String ak) {
        return mService.createStream(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_CREATE_STREAM, ak);
    }

    @Override
    public Observable<ZBCheckStreamPullJson> checkStream(String ak, String id) {
        return mService.checkStream(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_CHECK_STREAM, ak, id);
    }

    @Override
    public Observable<ZBApiJson> startStream(String ak, String title, String location) {
        return mService.startStream(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_START_STREAM, ak, title, location);
    }

    @Override
    public Observable<ZBEndStreamJson> endStream(String ak, String id) {
        return mService.endStream(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_END_STREAM, ak, id);
    }

    @Override
    public Observable<ZBBaseJson<ZBIconInfo[]>> upload(String ak, String thumb, File icon) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("api", ZBApi.API_UPLOAD_STREAM);
        builder.addFormDataPart("ak", ak);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), icon);//照片
        builder.addFormDataPart("cover", icon.getName(), body);
        builder.setType(MultipartBody.FORM);//设置类型
        MultipartBody multipartBody = builder.build();
        return mService.upload(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, multipartBody);

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

    /**
     * 主播禁言
     *
     * @param cid
     * @param time
     * @return
     */
    @Override
    public Observable<ZBApiJson> imDisable(String usid, int cid, long time, String ak) {
        return mService.imDisable(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_SHUTUP, ak, usid, cid, time);
    }

    /**
     * 主播解除禁言
     *
     * @param cid
     * @return
     */
    @Override
    public Observable<ZBApiJson> imEnable(String usid, int cid, String ak) {
        return mService.imEnable(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_SHUTUP_RESET, ak, usid, cid);
    }

}

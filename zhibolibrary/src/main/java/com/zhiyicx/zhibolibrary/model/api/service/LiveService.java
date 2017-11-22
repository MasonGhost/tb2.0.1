package com.zhiyicx.zhibolibrary.model.api.service;


import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.IconInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public interface LiveService {


    public static final String TYPE_UPLOAD_COVER = "cover";
    public static final String TYPE_UPLOAD_AVATAR = "avatar";
    public static final String TYPE_UPLOAD_STREAM = "stream";
    public static final String TYPE_UPLOAD_VERIFIED = "verified";




    /**
     * 上传单个文件
     *
     * @param accesskey
     * @param secretkey
     * @param type
     * @param icon
     * @return
     */
    @Multipart
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<IconInfo[]>> upload(@Part("api") String api, @Part("auth_accesskey") String accesskey,
                                            @Part("auth_secretkey") String secretkey,
                                            @Part("type") String type,
                                            @Part("icon\"; filename=\"icon.jpg\" ") RequestBody icon);

    /**
     * 上传多个文件
     *
     * @param multipartBody
     * @return
     */
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<IconInfo[]>> upload(@Body MultipartBody multipartBody);


}

package com.zhiyicx.zhibosdk.model.api.service;


import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBApiJson;
import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;
import com.zhiyicx.zhibosdk.model.entity.ZBApiStream;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBCheckStreamPullJson;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBIconInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
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
     * 创建新的流信息
     *
     * @param api
     * @param ak
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBApiStream> createStream(@Url String url, @Field("api") String api,
                                         @Field("ak") String ak);

    /**
     * 效验流信息
     *
     * @param api
     * @param ak
     * @param id  直播间的stream_key+stream_id(MD5加密)
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBCheckStreamPullJson> checkStream(
            @Url String url,
            @Field("api") String api,
            @Field("ak") String ak,
            @Field("stream_id") String id);

    /**
     * 开始推流准备
     *
     * @param api
     * @param ak
     * @param title    设置的直播间title
     * @param location GPS的经纬度坐标,格式 : 'lat纬度,lng经度'
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBApiJson> startStream(
            @Url String url,
            @Field("api") String api,
            @Field("ak") String ak,
            @Field("title") String title,
            @Field("location") String location);

    /**
     * 结束推流
     *
     * @param api
     * @param ak
     * @param id  直播间的stream_key+stream_id(MD5加密)
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBEndStreamJson> endStream(
            @Url String url,
            @Field("api") String api,
            @Field("ak") String ak,
            @Field("stream_id") String id);

    /**
     * 获得直播地址
     *
     * @param uid 当前直播间主播的UID
     * @param id  当前直播间的stream_key+stream_id(MD5加密)
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBApiPlay> getPlayUrl(
            @Url String url,
            @Field("api") String api,
            @Field("ak") String ak,
            @Field("stream_uid") String uid,
            @Field("stream_id") String id);


    /**
     * 上传单个文件
     *
     * @param type
     * @param icon
     * @return
     */
    @Multipart
    @POST
    Observable<ZBBaseJson<ZBIconInfo[]>> upload(
            @Url String url,
            @Part("api") String api,
            @Part("ak") String ak,
            @Part("type") String type,
            @Part("icon\"; filename=\"icon.jpg\" ") RequestBody icon);

    /**
     * 上传多个文件
     *
     * @param multipartBody
     * @return
     */
    @POST
    Observable<ZBBaseJson<ZBIconInfo[]>> upload(
            @Url String url,
            @Body MultipartBody multipartBody);

    /**
     * 主播设置禁言
     *
     * @param api
     * @param ak
     * @param usid
     * @param cid  当前直播间的聊天会话ID
     * @param time 禁言时长,单位分钟,0表示永久,default:0
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBApiJson> imDisable(
            @Url String url,
            @Field("api") String api,
            @Field("ak") String ak,
            @Field("usid") String usid,
            @Field("cid") int cid,
            @Field("time") long time
    );

    /**
     * 主播设置解除禁言
     *
     * @param api
     * @param ak
     * @param usid
     * @param cid  当前直播间的聊天会话ID
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBApiJson> imEnable(
            @Url String url,
            @Field("api") String api, @Field("ak") String ak,
            @Field("usid") String usid,
            @Field("cid") int cid
    );


}

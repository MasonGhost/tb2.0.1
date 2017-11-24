package com.zhiyicx.zhibosdk.model.api.service;


import com.google.gson.JsonObject;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public interface ZBCommonService<T> {


    /**
     * 通过票据兑换用户的授权信息 (类 :Gold,方法:applePayBack)
     *
     * @param ticket
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<ZBUserAuth>> getUserAuthByTicket(@Url String url,@Field("api") String api, @Field("ticket") String ticket
    );


    /**
     * 应用验证
     * AUTH_APPID:应用appid(在智播申请到的appid)
     * AUTH_TOKEN:由十进制时间戳+原始的token(在智播申请到的token) 进行MD5加密的字符串
     * AUTH_BASETIME:编码的时间,十六进制的时间戳
     *
     * @param appId
     * @param token    MD5(当前时间戳+口令(zhibo_secret))
     * @param basetime 16进制(当前时间戳)
     * @return
     */
    @POST
    Observable<ZBBaseJson<String>> getApi(@Url String url, @Header("Auth-Appid") String appId,
                                          @Header("Auth-Token") String token,
                                          @Header("Auth-Basetime") String basetime);
    /**
     * 应用数据域名
     * AUTH_APPID:应用appid(在智播申请到的appid)
     * AUTH_TOKEN:由十进制时间戳+原始的token(在智播申请到的token) 进行MD5加密的字符串
     * AUTH_BASETIME:编码的时间,十六进制的时间戳
     *
     * @param url
     * @param api
     * @param hextime
     * @param token    MD5(当前时间戳+口令(zhibo_secret))
     * @param api_version
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<String>> getDomain(@Url String url, @Field("api") String api, @Field("hextime") String hextime,
                                             @Field("token") String token,
                                             @Field("api_version") String api_version);


    /**
     * 获得配置信息
     * <p/>
     * name(★ name 参数计算方法:
     * ① 指定要获得配置 如 : name = ‘token_key,send_sns_token’
     * ② 将name的值进行urlencode编码,则此时的name为:
     * name = ‘tokenKey%2Csend_sns_token’
     * ③ 最后进行 base64编码,name的最终参数为 :
     * name = ‘dG9rZW5LZXklMkNzZW5kX3Nuc190b2tlbg==’)
     *
     * @return
     */

    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<ZBApiConfig>> getConfig(@Url String url,@Field("api") String api, @Field("hextime") String hextime,
                                                  @Field("token") String token,
                                                  @Field("name") String name);


    /**
     * @param hextime 转换当前时间戳为十六进制时间戳
     * @param token   将当前时间戳+十六进制的时间戳进行MD5加密得到token口令
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<String>> getApiVersion(@Url String url,@Field("api") String api,
                                                 @Field("hextime") String hextime,
                                                 @Field("token") String token);

    /**
     * 直播云api
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<JsonObject> getCommonApi(@Url
    String url,
            @FieldMap Map<String, Object> fields);

    /**
     * 下载大型文件
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    @FormUrlEncoded
    @Streaming
    @POST
    Observable<ResponseBody> downLoadFilterWord(@Url String url,@Field("api") String api, @Field("hextime") String hextime,
                                                @Field("token") String token,
                                                @Field("name") String name);

}

package com.zhiyicx.zhibolibrary.model.api.service;

import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.Config;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public interface CommonService {


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
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<Config>> getConfig(@Field("api") String api, @Field("hextime") String hextime,
                                           @Field("token") String token,
                                           @Field("name") String name);


    /**
     * @param hextime 转换当前时间戳为十六进制时间戳
     * @param token   将当前时间戳+十六进制的时间戳进行MD5加密得到token口令
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<String>> getApiVersion(@Field("api") String api,
                                               @Field("hextime") String hextime,
                                               @Field("token") String token);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}

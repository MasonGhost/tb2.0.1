package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.LoginBean;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public interface CommonClient {
    /**
     * 验证码类型
     * register: 注册
     * login: 登录
     * change: 修改,找回密码
     */

    public static final String VERTIFY_CODE_TYPE_REGISTER = "register";
    public static final String VERTIFY_CODE_TYPE_LOGIN = "login";
    public static final String VERTIFY_CODE_TYPE_CHANGE = "change";

    /**
     * @param requestState {requestState}=success/fasle
     * @param phone        需要被发送验证码的手机号
     * @param type         发送验证码的类型，固定三个值(register、login、change) register: 注
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<CacheBean>> getVertifyCode(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("type") String type);

    /**
     * 刷新token
     *
     * @param deviceCode  设备号
     * @param refrshToken 刷新token
     * @return 成功后自动调用auth接口，返回信息和login一样
     */
    @PATCH("api/v1/auth")
    Observable<LoginBean> refreshToken(@Query("refresh_token") String refrshToken, @Query("device_code") String deviceCode);


    ///////////////////////////////////////文件上传////////////////////////////////////////////////////

    /**
     * 储存任务创建
     *
     * @param hash            待上传文件hash值，hash方式md5
     * @param origin_filename 原始文件名称
     */
    @GET("api/v1/storages/task/{hash}/{origin_filename}")
    Observable<BaseJson<StorageTaskBean>> createStorageTask(@Path("hash") String hash, @Path("origin_filename") String origin_filename, @Query("requestState") String requestState);

    /**
     * 通过Post方法上传文件
     */
    @Multipart
    @POST
    Observable<String> upLoadFileByPost(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> partList);

    /**
     * 通过Put方法上传文件
     */
    @Multipart
    @PUT
    Observable<String> upLoadFileByPut(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> partList);


    /**
     * 储存任务通知
     *
     * @param storage_task_id 任务ID
     * @param message         附加上传接口返回的原样数据
     * @return 返回将以通用message格式返回上传的附件是成功还是失败等状态信息。
     */
    @FormUrlEncoded
    @PATCH("api/v1/storages/task/{storage_task_id}")
    Observable<BaseJson> notifyStorageTask(@Path("storage_task_id") String storage_task_id, @Field("message") String message, @Query("requestState") String requestState);

    /**
     * 通知服务器，删除当前上传文件
     *
     * @param storage_task_id 任务ID
     */
    @DELETE("api/v1/storages/task/{storage_task_id}")
    Observable<BaseJson> deleteStorageTask(@Path("storage_task_id") String storage_task_id, @Query("requestState") String requestState);

}

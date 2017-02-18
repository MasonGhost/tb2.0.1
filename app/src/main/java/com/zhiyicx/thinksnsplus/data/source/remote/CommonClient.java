package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CREATE_STORAGE_TASK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DELETE_STORAGE_TASK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_VERTIFYCODE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_HANDLE_BACKGROUND_TASK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_NOTIFY_STORAGE_TASK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REFRESH_TOKEN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_TOKEN_EXPIERD;

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
     * 获取验证码
     *
     * @param requestState {requestState}=success/fasle
     * @param phone        需要被发送验证码的手机号
     * @param type         发送验证码的类型，固定三个值(register、login、change) register: 注
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_GET_VERTIFYCODE)
    Observable<BaseJson<CacheBean>> getVertifyCode(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("type") String type);

    /**
     * 刷新 token
     *
     * @param deviceCode  设备号
     * @param refrshToken 刷新token
     * @return 成功后自动调用auth接口，返回信息和login一样
     */
    @PATCH(APP_PATH_REFRESH_TOKEN)
    Observable<BaseJson<AuthBean>> refreshToken(@Query("refresh_token") String refrshToken, @Query("device_code") String deviceCode);


    ///////////////////////////////////////文件上传////////////////////////////////////////////////////

    /**
     * 储存任务创建
     *
     * @param hash            待上传文件hash值，hash方式md5
     * @param origin_filename 原始文件名称
     */
    @POST(APP_PATH_CREATE_STORAGE_TASK)
    Observable<BaseJson<StorageTaskBean>> createStorageTask(@Path("hash") String hash, @Path("origin_filename") String origin_filename, @Query("requestState") String requestState);

    /**
     * 通过Post方法上传文件
     */
    @Multipart
    @POST
    Observable<String> upLoadFileByPost(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> params);

    /**
     * 通过Put方法上传文件
     */
    @Multipart
    @PUT
    Observable<String> upLoadFileByPut(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> params);


    /**
     * 储存任务通知
     *
     * @param storage_task_id 任务ID
     * @param message         附加上传接口返回的原样数据
     * @return 返回将以通用message格式返回上传的附件是成功还是失败等状态信息。
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_NOTIFY_STORAGE_TASK)
    Observable<BaseJson> notifyStorageTask(@Path("storage_task_id") String storage_task_id, @Field("message") String message, @Query("requestState") String requestState);

    /**
     * 通知服务器，删除当前上传文件
     *
     * @param storage_task_id 任务ID
     */
    @DELETE(APP_PATH_DELETE_STORAGE_TASK)
    Observable<BaseJson> deleteStorageTask(@Path("storage_task_id") String storage_task_id, @Query("requestState") String requestState);


    /*******************************************  后台任务处理  *********************************************/

    /**
     * 后台任务处理
     */
    @Multipart
    @POST(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJson<CacheBean>> handleBackGroundTaskPost(@Path("path") String path, @PartMap Map<String, Object> bodyMap);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @HTTP(method = "DELETE", path = APP_PATH_HANDLE_BACKGROUND_TASK, hasBody = true)
    Observable<BaseJson<CacheBean>> handleBackGroudTaskDelete(@Path("path") String path, @Body RequestBody requestBody);


    /**
     * rap接口，用来测试token过期,当前返回token过期
     *
     * @return
     */
    @POST(APP_PATH_TOKEN_EXPIERD)
    Observable<BaseJson> testTokenExpierd(@Query("requestState") String requestState);

}

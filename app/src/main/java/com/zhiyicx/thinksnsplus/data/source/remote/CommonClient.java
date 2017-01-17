package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
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


    ///////////////////////////////////////文件上传////////////////////////////////////////////////////

    /**
     * 储存任务创建
     *
     * @param hash            待上传文件hash值，hash方式md5
     * @param origin_filename 原始文件名称
     */
    @Multipart
    @POST("api/v1/storages/task/{hash}/{origin_filename}")
    Observable<BaseJson<StorageTaskBean>> createStorageTask(@Path("hash") String hash, @Path("origin_filename") String origin_filename, @Part List<MultipartBody.Part> partList);

    /**
     * 储存任务通知
     *
     * @param storage_task_id 任务ID
     * @param message         附加上传接口返回的原样数据
     * @return 返回将以通用message格式返回上传的附件是成功还是失败等状态信息。
     */
    @PATCH("api/v1/storages/task/{storage_task_id}")
    Observable<BaseJson> notifyStorageTask(@Path("storage_task_id") String storage_task_id, @Field("message") String message);

    /**
     * 通知服务器，删除当前上传文件
     *
     * @param storage_task_id 任务ID
     */
    @DELETE("api/v1/storages/task/{storage_task_id}")
    Observable<BaseJson> deleteStorageTask(@Path("storage_task_id") String storage_task_id);

}

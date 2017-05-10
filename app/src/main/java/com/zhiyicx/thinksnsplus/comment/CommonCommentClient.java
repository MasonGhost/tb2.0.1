package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_HANDLE_BACKGROUND_TASK;

/**
 * @Author Jliuer
 * @Date 2017/04/27/17:54
 * @Email Jliuer@aliyun.com
 * @Description 暂未用到
 */
public interface CommonCommentClient {

    @Multipart
    @POST(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJson<Object>> sendComment(@Path("path") String path, @Part List<MultipartBody.Part> partList);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @HTTP(method = "DELETE", path = APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJson<CacheBean>> deleteComment(@Path("path") String path);
}

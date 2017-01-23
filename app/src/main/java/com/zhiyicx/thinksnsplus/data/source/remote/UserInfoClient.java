package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.HashMap;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 用户信息相关的网络请求
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public interface UserInfoClient {
    // 上传头像功能，写在CommonClient中

    /**
     * 修改用户资料
     *
     * @param userFieldMap 用户需要修改哪那些信息不确定
     */
    @FormUrlEncoded
    @PATCH("api/v1/users")
    Observable<BaseJson> changeUserInfo(@FieldMap HashMap<String, String> userFieldMap);


    /**
     * 创建对话
     *
     * @param type 对话类型 `0` 私有对话 `1` 群组对话 `2`聊天室对话
     * @param name 对话名称
     * @param pwd  对话加入密码,type=`0`时该参数无效
     * @param uids 对话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/im/conversations")
    Observable<BaseJson<Conversation>> createConversaiton(@Field("type") String type, @Field("name") String name, @Field("pwd") String pwd, @Field("uids") String uids);

    /**
     * 获取用户信息
     * @param user_id
     * @return
     */
    @GET("/api/v1/users")
    Observable<BaseJson<UserInfoBean>> getUserInfo(@Query("user") int user_id);
}

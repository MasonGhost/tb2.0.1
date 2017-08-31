package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.imsdk.entity.Conversation;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CREATE_CONVERSAITON;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CONVERSAITON_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_SINGLE_CONVERSAITON;

/**
 * @author jungle68
 * @describe 聊天信息相关的网络请求
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public interface ChatInfoClient {

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
    @POST(APP_PATH_CREATE_CONVERSAITON)
    Observable<Conversation> createConversaiton(@Field("type") int type, @Field("name") String name, @Field("pwd") String pwd, @Field("uids") String uids);

    /**
     * 获取登陆用户的对话列表
     *
     * @return
     */
    @GET(APP_PATH_GET_CONVERSAITON_LIST)
    Observable<List<Conversation>> getConversaitonList();

    /**
     * 获取单个对话信息
     *
     * @param cid 对话 id  特别说明 地址中的cid为对话id,如果该对话id不存在会返回错误
     * @return
     */
    @GET(APP_PATH_GET_SINGLE_CONVERSAITON)
    Observable<Conversation> getSingleConversaiton(@Path("cid") int cid);


}

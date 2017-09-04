package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.request.DeleteUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.UpdateUserPhoneOrEmailRequestBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_BIND_WITH_INPUT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_BIND_WITH_LOGIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CANDEL_BIND;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_BIND_OR_GET_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_IN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_REGISTER_OR_GET_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BIND_THIRDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CHECK_IN_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CHECK_IN_RANKS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_HOT_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_NEW_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_RECOMMENT_BY_TAG_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_USER_AROUND;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_UPDATE_USER_LOCATION;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public interface UserInfoContract {
    interface View extends IBaseView<UserInfoContract.Presenter> {
        /**
         * 设置地域选择的数据
         */
        void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items, ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items);

        /**
         * 设置头像上传的状态
         *
         * @param upLoadState -1 失败 0进行中 1 图片上传成功 2图片用户信息修改成功
         */
        void setUpLoadHeadIconState(int upLoadState);

        /**
         * 设置信息修改提交状态
         *
         * @param changeUserInfoState -1 失败 1进行中 2 成功
         */
        void setChangeUserInfoState(int changeUserInfoState, String message);

        /**
         * 初始化界面数据
         */
        void initUserInfo(UserInfoBean mUserInfoBean);

        /**
         * 更新用户标签
         *
         * @param datas tags
         */
        void updateTags(List<UserTagBean> datas);
    }

    interface Repository {
        /**
         * 从本地文件获取全国所有的省市
         */
        Observable<ArrayList<AreaBean>> getAreaList();

        /**
         * 编辑用户信息
         *
         * @param userInfos 用户需要修改的信息，通过 hashMap 传递，key 表示请求字段，value 表示修改的值
         */
        Observable<Object> changeUserInfo(HashMap<String, Object> userInfos);

        /**
         * 获取用户信息
         *
         * @param user_ids 用户 id 数组
         * @return
         */
        Observable<List<UserInfoBean>> getUserInfo(List<Object> user_ids);


        /**
         * <p>获取当前登录用户信息<p>
         *
         * @return
         */
        Observable<UserInfoBean> getCurrentLoginUserInfo();

        /**
         * 获取指定用户信息  其中 following、follower 是可选参数，验证用户我是否关注以及是否关注我的用户 id ，默认为当前登陆用户。
         *
         * @param userId          the specified user id
         * @param followingUserId following user id
         * @param followerUserId  follow user id
         * @return
         */
        Observable<UserInfoBean> getSpecifiedUserInfo(long userId, Long followingUserId, Long followerUserId);

        /**
         * 批量获取指定用户的用户信息
         *
         * @param user_ids user 可以是一个值，或者多个值，多个值的时候用英文半角 , 分割。
         * @return
         */
        Observable<List<UserInfoBean>> getUserInfoByIds(String user_ids);

        /**
         * 搜索用户的用户信息
         *
         * @param user_ids Get multiple designated users, multiple IDs using , split.
         * @param name     Used to retrieve users whose username contains name.
         * @param since    The integer ID of the last User that you've seen.
         * @param order    Sorting. Enum: asc, desc
         * @param limit    List user limit, minimum 1 max 50.
         * @return
         */
        Observable<List<UserInfoBean>> searchUserInfo(String user_ids, String name, Integer since, String order, Integer limit);

        /**
         * 获取用户信息,先从本地获取，本地没有再从网络 获取
         *
         * @param user_id 用户 id
         * @return
         */
        Observable<UserInfoBean> getLocalUserInfoBeforeNet(long user_id);

        /**
         * 关注操作
         *
         * @param followFansBean
         */
        void handleFollow(UserInfoBean followFansBean);

        /**
         * 获取用户收到的点赞
         *
         * @param max_id
         * @return
         */
        Observable<List<DigedBean>> getMyDiggs(int max_id);


        /**
         * 获取用户收到的评论
         *
         * @param max_id
         * @return
         */
        Observable<List<CommentedBean>> getMyComments(int max_id);

        /**
         * 更新认证用户的手机号码和邮箱
         * @param phone
         * @param email
         * @param verifiable_code
         * @return
         */
        Observable<Object> updatePhoneOrEmail(String phone, String email, String verifiable_code);

        /**
         * 解除用户 Phone 绑定:
         *
         * @param password
         * @param verify_code
         * @return
         */
        Observable<Object> deletePhone(String password, String verify_code);

        /**
         * 解除用户 E-Mail 绑定:
         *
         * @param password
         * @param verify_code
         * @return
         */
        Observable<Object> deleteEmail(String password, String verify_code);


        /*******************************************  标签  *********************************************/

        /**
         * 获取一个用户的标签
         *
         * @param user_id
         * @return
         */
        Observable<List<UserTagBean>> getUserTags(long user_id);

        /**
         * 获取当前认证用户的标签
         *
         * @return
         */
        Observable<List<UserTagBean>> getCurrentUserTags();

        /**
         * 当前认证用户附加一个标签
         *
         * @param tag_id
         * @return
         */
        Observable<Object> addTag(long tag_id);

        /**
         * 当前认证用户分离一个标签
         *
         * @param tag_id
         * @return
         */
        Observable<Object> deleteTag(long tag_id);

        /*******************************************  找人  *********************************************/


        /**
         * 热门用户
         *
         * @param limit  每页数量
         * @param offset 偏移量, 注: 此参数为之前获取数量的总和
         * @return
         */
        Observable<List<UserInfoBean>> getHotUsers(Integer limit, Integer offset);

        /**
         * 最新用户
         *
         * @param limit  每页数量
         * @param offset 偏移量, 注: 此参数为之前获取数量的总和
         * @return
         */
        Observable<List<UserInfoBean>> getNewUsers(Integer limit, Integer offset);

        /**
         * tag 推荐用户
         *
         * @param limit  每页数量
         * @param offset 偏移量, 注: 此参数为之前获取数量的总和
         * @return
         */
        Observable<List<UserInfoBean>> getUsersRecommentByTag(Integer limit, Integer offset);

        /**
         *
         * @return 后台推荐用户
         */
        Observable<List<UserInfoBean>> getRecommendUserInfo();
        /**
         * phone 推荐用户
         * <p>
         * { "phones": [ 18877778888, 18999998888, 17700001111 ] }
         *
         * @return
         */
        Observable<List<UserInfoBean>> getUsersByPhone(ArrayList<String> phones);


        /**
         * 更新位置数据
         *
         * @param longitude 经度
         * @param latitude  纬度
         * @return
         */
        Observable<Object> updateUserLocation(double longitude, double latitude);

        /**
         * 根据经纬度查询周围最多 50KM 内的 TS+ 用户
         *
         * @param longitude 当前用户所在位置的纬度
         * @param latitude  当前用户所在位置的经度
         * @param radius    搜索范围，米为单位 [0 - 50000], 默认3000
         * @param limit     默认20， 最大100
         * @param page      分页参数， 默认1，当返回数据小于limit， page达到最大值
         * @return
         */
        Observable<List<NearbyBean>> getNearbyData(double longitude, double latitude, Integer radius, Integer limit, Integer page);


        /*******************************************  签到  *********************************************/

        /**
         * 获取签到信息
         *
         * @return
         */
        Observable<CheckInBean> getCheckInInfo();

        /**
         * 签到
         *
         * @return
         */
        Observable<Object> checkIn();

        /**
         * 连续签到排行榜
         *
         * @param offset 数据偏移数，默认为 0。
         * @return
         */
        Observable<List<UserInfoBean>> getCheckInRanks(Integer offset);


        /*******************************************  三方登录  *********************************************/

        /**
         * 获取已经绑定的三方
         * qq	    腾讯 QQ 。
         * weibo	新浪 Weibo 。
         * wechat	腾讯微信 。
         *
         * @return 请求成功后，将返回用户已绑定第三方的 provider 名称，不存在列表中的代表用户并为绑定。
         */
        Observable<List<String>> getBindThirds();

        /**
         * 检查绑定并获取用户授权
         *
         * @param access_token thrid token
         * @return 返回的数据参考 「用户／授权」接口，如果返回 404 则表示没有改账号没有注册，进入第三方登录注册流程。
         */
        Observable<AuthBean> checkThridIsRegitser(String provider, String access_token);

        /**
         * 检查注册信息或者注册用户
         *
         * @param provider     type qq\weibo\wechat
         * @param access_token 获取的 Provider Access Token。
         * @param name         用户名。
         * @param check        如果是 null 、 false 或 0 则不会进入检查，如果 存在任何转为 bool 为 真 的值，则表示检查注册信息。
         * @return
         */
        Observable<AuthBean> checkUserOrRegisterUser(String provider, String access_token, String name, Boolean check);

        /**
         * 已登录账号绑定
         *
         * @param provider
         * @param access_token
         * @return
         */
        Observable<Object> bindWithLogin(String provider, String access_token);

        /**
         * 输入账号密码绑定
         *
         * @param provider     type qq\weibo\wechat
         * @param access_token 获取的 Provider Access Token。
         * @param login        用户登录名，手机，邮箱
         * @param password     用户密码。
         * @return
         */
        Observable<AuthBean> bindWithInput(String provider, String access_token, String login, String password);

        /**
         * 取消绑定
         *
         * @param provider type qq\weibo\wechat
         * @return
         */
        Observable<Object> cancelBind(String provider);


    }

    interface Presenter extends IBasePresenter {
        /**
         * 上传用户头像
         *
         * @param filePath
         */
        void changeUserHeadIcon(String filePath);

        /**
         * @param userInfos
         * @param isHeadIcon 仅仅修改头像
         */
        void changUserInfo(HashMap<String, Object> userInfos, boolean isHeadIcon);


        /**
         * 初始化用户界面数据
         */
        void initUserInfo();

        void getCurrentUserTags();
    }
}

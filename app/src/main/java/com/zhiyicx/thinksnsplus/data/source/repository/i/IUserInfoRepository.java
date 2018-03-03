package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskRewardRuleBean;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.rank.RankData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Describe 用户相关
 * @Author Jungle68
 * @Date 2017/12/19
 * @Contact master.jungle68@gmail.com
 */

public interface IUserInfoRepository {

    /**
     * 注册
     *
     * @param phone       注册的手机号码
     * @param name        用户名
     * @param vertifyCode 手机验证码
     * @param password    用户密码
     * @return
     */
    Observable<AuthBean> registerByPhone(String phone, String name, String vertifyCode, String password);

    /**
     * @param email       注册的邮箱
     * @param name        用户名
     * @param vertifyCode 邮箱验证码
     * @param password    用户密码
     * @return
     */
    Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password);

    Observable<AuthBean> loginV2(final String account, final String password);

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
     *
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

    /**
     * 获取认证信息
     *
     * @return
     */
    Observable<UserCertificationInfo> getCertificationInfo();

    /**
     * 提交认证信息
     *
     * @param bean
     * @return
     */
    Observable<BaseJsonV2<Object>> sendCertification(SendCertificationBean bean);


    Observable<List<UserInfoBean>> getFollowListFromNet(long userId, int maxId);

    Observable<List<UserInfoBean>> getFansListFromNet(long userId, int maxId);

    Observable<Object> followUser(long followedId);

    Observable<Object> cancleFollowUser(long followedId);
    /*******************************************  TB  *********************************************/

    /**
     * 财富排行榜
     */
    Observable<List<RankData>> getTBRank(Long limit, int size);

    /**
     * @param limit limit	int	条目数
     * @param size  offset	int	翻页标示
     * @param type  type	string	默认: all, all-累计贡献排行 day-日贡献排行
     * @return
     */
    Observable<List<ContributionData>> getContributionRank(Long limit, int size, String type);

    /**
     *
     * @return 快讯分享统计
     */
    Observable<BaseJsonV2> shareCount(String type, String id);

    /**
     * 获取任务信息
     *
     * @return
     */
    Observable<TBTaskContainerBean> getTaskInfo();

    /**
     * 获取任务奖励说明
     *
     * @return
     */
    Observable<TBTaskRewardRuleBean> getTaskRewardRule();
}

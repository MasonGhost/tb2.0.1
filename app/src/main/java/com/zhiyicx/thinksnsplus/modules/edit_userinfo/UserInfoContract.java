package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

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
        Observable<BaseJson> changeUserInfo(HashMap<String, Object> userInfos);

        /**
         * 获取用户信息
         *
         * @param user_ids 用户 id 数组
         * @return
         */
        Observable<BaseJson<List<UserInfoBean>>> getUserInfo(List<Object> user_ids);


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
         *  搜索用户的用户信息
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
        Observable<BaseJson<UserInfoBean>> getLocalUserInfoBeforeNet(long user_id);

        /**
         * 获取用户关注状态
         */
        Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids);

        /**
         * 关注操作
         *
         * @param followFansBean
         */
        void handleFollow(UserInfoBean followFansBean);

        /**
         * 获取点赞排行榜
         *
         * @param page
         * @return
         */
        Observable<BaseJson<List<DigRankBean>>> getDidRankList(int page);

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
         * @param time 零时区的秒级时间戳
         * @param key  查询关键字 默认查询全部 多个以逗号隔开 可选参数有 diggs comments follows
         * @return
         */
        Observable<BaseJson<List<FlushMessages>>> getMyFlushMessage(long time, String key);
    }

    interface Presenter extends IBasePresenter {
        void getAreaData();

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

    }
}

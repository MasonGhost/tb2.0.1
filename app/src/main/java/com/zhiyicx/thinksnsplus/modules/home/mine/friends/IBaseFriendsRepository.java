package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe 好友的公用Repository
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public interface IBaseFriendsRepository {
    /**
     * 获取用户好友列表
     *
     * @param maxId offset
     * @param keyword 搜索用的关键字
     * @return Observable
     */
    Observable<List<UserInfoBean>> getUserFriendsList(long maxId, String keyword);
}

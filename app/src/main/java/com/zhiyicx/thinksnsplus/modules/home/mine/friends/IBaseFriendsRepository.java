package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
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
     * @param maxId   offset
     * @param keyword 搜索用的关键字
     * @return Observable
     */
    Observable<List<UserInfoBean>> getUserFriendsList(long maxId, String keyword);

    /**
     * 创建群组会话
     * groupName 	// 群组名称（必填）
     * groupIntro 		// 群组简介（必填）
     * isPublic 		// 是否是公开群，此属性为必须的（必填）
     * maxUser	// 群组成员最大数
     * isMemberOnly	// 加入群是否需要群主或者群管理员审批，默认是false
     * isAllowInvites	// 是否允许群成员邀请别人加入此群
     * owner		// 群组的管理员（必填）
     * members		// 群组的成员
     */
    Observable<ChatGroupBean> createGroup(String groupName, String groupIntro, boolean isPublic,
                                          int maxUser, boolean isMemberOnly, boolean isAllowInvites,
                                          long owner, String members);
}

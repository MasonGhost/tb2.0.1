package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 消息相关
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public interface IMessageRepository {
    /**
     * 获取对话列表信息
     *
     * @param user_id 用户 id
     * @return
     */
    Observable<List<MessageItemBean>> getConversationList(int user_id);

    /**
     * 通过 对话 id 获取对话信息
     *
     * @param cid 对话 id
     * @return
     */
    Observable<MessageItemBean> getSingleConversation(int cid);

    /**
     * 未读通知数量检查
     *
     * @return
     */
    Observable<Void> ckeckUnreadNotification();


    /**
     * 获取用户未读消息
     *
     * @return
     * @see {https://slimkit.github.io/plus-docs/v2/core/users/unread#用户未读消息}
     */
    Observable<UnReadNotificaitonBean> getUnreadNotificationData();

    /**
     * 获取通知列表
     *
     * @param notification
     * @param type
     * @param limit
     * @param offset
     * @return
     */
    Observable<List<TSPNotificationBean>> getNotificationList(String notification, String type, Integer limit, Integer offset);

    /**
     * 读取通知
     *
     * @param notificationId
     * @return
     */
    Observable<TSPNotificationBean> getNotificationDetail(String notificationId);

    /**
     * 标记通知阅读
     *
     * @param notificationId
     * @return
     */
    Observable<Object> makeNotificationReaded(String notificationId);

    /**
     * 标记所有通知阅读
     *
     * @return
     */
    Observable<Object> makeNotificationAllReaded();


    Observable<List<MessageItemBeanV2>> getConversationListV2(int user_id);


    Observable<List<MessageItemBeanV2>> completeEmConversation(List<MessageItemBeanV2> list);
}

package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMConversation;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public interface ChatContract {

    interface View extends IBaseView<Presenter> {
        /**
         * 设置聊天头信息
         *
         * @param titleStr 头内容
         */
        void setChatTitle(@NotNull String titleStr);

        /**
         * 刷新聊天内容
         *
         * @param chatItemBean 消息内容
         */
        void reFreshMessage(ChatItemBean chatItemBean);

        /**
         * 刷新列表数据
         */
        void refreshData();

        /**
         * 滑动内容到底部
         */
        void smoothScrollToBottom();

        /**
         * 获取当前对话 id
         *
         * @return 当前对话 id
         */
        int getCurrentChatCid();

        /**
         * 刷新消息状态
         */
        void updateMessageStatus(Message message);

        /**
         * 更新对话信息
         */
        void updateConversation(EMConversation conversation);

        /**
         * 获取会话的信息
         *
         * @return MessageItemBeanV2
         */
        MessageItemBeanV2 getMessItemBean();

        /**
         * 获取历史聊天记录成功
         *
         * @param list 历史聊天记录
         * @param isInit 是否为初始化
         */
        void getHistoryMessageSuccess(List<ChatItemBean> list, boolean isInit);

    }

    interface Repository {
        /**
         * 创建对话
         *
         * @param type 会话类型 `0` 私有会话 `1` 群组会话 `2`聊天室会话
         * @param name 会话名称
         * @param pwd  会话加入密码,type=`0`时该参数无效
         * @param uids 会话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
         */
        Observable<Conversation> createConveration(int type, String name, String pwd, String uids);

        /**
         * 插入或者更新数据库
         *
         * @param conversation 对话信息
         */
        boolean insertOrUpdateConversation(Conversation conversation);

        /**
         * 插入或者更新数据库
         *
         * @param data 对话列表
         */
        boolean insertOrUpdateMessageItemBean(List<MessageItemBean> data);

        /**
         * 获取聊天对话列表信息
         */
        List<MessageItemBean> getConversionListData(long userId);

        /**
         * 获取聊天列表信息
         */
        List<ChatItemBean> getChatListData(int cid, long mid);

        /**
         * 获取环信的聊天记录列表
         *
         * @param itemBeanV2 会话信息
         * @param msgId      当前的第一条的消息id
         * @param pageSize   一页数量
         * @return List<ChatItemBean>
         */
        List<ChatItemBean> getChatListDataV2(MessageItemBeanV2 itemBeanV2, String msgId, int pageSize);

        /**
         * 根据每条聊天 来完善用户信息
         *
         * @return Observable<List<ChatItemBean>>
         */
        Observable<List<ChatItemBean>> completeUserInfo(List<ChatItemBean> list);

    }

    interface Presenter extends IBasePresenter {
        /**
         * 获取用户信息
         *
         * @param user_id 用户 id
         */
        void getUserInfo(long user_id);

        /**
         * 获取房间历史信息
         *
         * @param cid 对话 id
         * @param mid 聊天内容创建时间
         * @return 聊天信息
         */
        List<ChatItemBean> getHistoryMessages(int cid, long mid);

        /**
         * 获取环信的历史消息
         *
         * @param id       聊天的id 单聊对方用户id 群聊则为群聊的id
         * @param pageSize 一页大小
         * @return List<ChatItemBean>
         */
        List<ChatItemBean> getHistoryMessagesV2(String id, int pageSize);

        /**
         * 发送文本消息
         *
         * @param text 文本内容
         * @param cid  对话 id
         */
        void sendTextMessage(String text, int cid);

        /**
         * 发送环信文本消息
         *
         * @param content 内容
         * @param userId  用户id即为会话id 群聊则为群聊的id，就是这样喵
         */
        void sendTextMessageV2(String content, String userId);

        /**
         * 消息重发
         */
        void reSendText(ChatItemBean chatItemBean);

        /**
         * 创建对话
         *
         * @param userInfoBean 目标对象的用户信息
         * @param text         文本消息，单独创建的时候，传空
         */
        void createChat(UserInfoBean userInfoBean, String text);

        String checkTShelper(long user_id);
    }
}

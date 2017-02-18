package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

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
         * 刷新聊天内容
         *
         * @param chatItemBean 消息内容
         */
        void reFreshMessage(ChatItemBean chatItemBean);

        void smoothScrollToBottom();

    }

    interface Repository {
        /**
         * 创建对话
         *
         * @param type 会话类型 `0` 私有会话 `1` 群组会话 `2`聊天室会话
         * @param name 会话名称
         * @param pwd  会话加入密码,type=`0`时该参数无效
         * @param uids 会话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
         * @return
         */
        Observable<BaseJson<Conversation>> createConveration(int type, String name, String pwd, String uids);

        /**
         * 插入或者更新数据库
         *
         * @param conversation 对话信息
         * @return
         */
        boolean insertOrUpdateConversation(Conversation conversation);

        /**
         * 插入或者更新数据库
         *
         * @param data 对话列表
         * @return
         */
        boolean insertOrUpdateMessageItemBean(List<MessageItemBean> data);

        /**
         * 获取聊天对话列表信息
         *
         * @param userId
         * @return
         */
        List<MessageItemBean> getConversionListData(long userId);

        /**
         * @param cid
         * @param mid
         * @return
         */
        List<ChatItemBean> getChatListData(int cid, long mid);

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
         * 发送文本消息
         *
         * @param text 文本内容
         * @param cid  对话 id
         */
        void sendTextMessage(String text, int cid);

    }
}

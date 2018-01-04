package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseMessageRepository;

import java.util.List;


/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */

public interface MessageConversationContract {

    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends ITSListView<MessageItemBean, Presenter> {

        /**
         * 获取环信消息列表成功
         *
         * @param list 消息列表
         */
        void getMessageListSuccess(List<MessageItemBeanV2> list);

        /**
         * 获取环信的消息列表
         *
         * @return list
         */
        List<MessageItemBeanV2> getRealMessageList();
    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository extends IBaseMessageRepository {
    }

    interface Presenter extends ITSListPresenter<MessageItemBean> {

        /**
         * 刷新是否显示底部红点
         * 刷新当条item 的未读数
         */
        void refreshConversationReadMessage();

        /**
         * 删除本地对话
         *
         * @param position 位置
         */
        void deleteConversation(int position);

        /**
         * 检查当前消息记录
         */
        void handleFlushMessage();


        void checkUnreadNotification();

        /**
         * 获取聊天用户列表
         *
         * @param position 点击位置
         * @return List<ChatUserInfoBean>
         */
        List<ChatUserInfoBean> getChatUserList(int position);

        void searchList(String key);
    }
}

package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ISystemRepository;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface SystemConversationContract {

    interface View extends IBaseView<Presenter> {


    }

    interface Repository extends ISystemRepository{


    }

    interface Presenter extends IBasePresenter {

        /**
         * 发送文本消息
         *
         * @param text 文本内容
         * @param cid  对话 id
         */
        void sendTextMessage(String text, int cid);

        /**
         * 消息重发
         *
         * @param chatItemBean
         */
        void reSendText(ChatItemBean chatItemBean);


    }
}

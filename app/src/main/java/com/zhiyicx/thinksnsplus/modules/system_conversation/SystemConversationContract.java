package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ISystemRepository;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface SystemConversationContract {

    interface View extends IBaseView<Presenter> {


    }

    interface Repository extends ISystemRepository {

        List<SystemConversationBean> requestCacheData(long max_Id);

    }

    interface Presenter extends IBasePresenter {

        /**
         * 发送文本消息
         *
         * @param text 文本内容
         */
        void sendTextMessage(String text);

        /**
         * 请求列表数据
         *
         * @param maxId      当前获取到数据的最大 id
         * @param isLoadMore true 加载更多，false 刷新
         */
        void requestNetData(Long maxId, boolean isLoadMore);

        /**
         * 获取本地数据
         *
         * @param max_Id
         * @return
         */
        void requestCacheData(long max_Id);
    }
}

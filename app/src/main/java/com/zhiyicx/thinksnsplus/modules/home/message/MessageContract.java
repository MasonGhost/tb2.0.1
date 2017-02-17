package com.zhiyicx.thinksnsplus.modules.home.message;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */

public interface MessageContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends ITSListView<MessageItemBean, Presenter> {
        void updateCommnetItemData(MessageItemBean messageItemBean);

        void updateLikeItemData(MessageItemBean messageItemBean);

    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository {
    }

    interface Presenter extends ITSListPresenter<MessageItemBean> {
        MessageItemBean updateCommnetItemData();

        MessageItemBean updateLikeItemData();

        void createChat();

    }
}

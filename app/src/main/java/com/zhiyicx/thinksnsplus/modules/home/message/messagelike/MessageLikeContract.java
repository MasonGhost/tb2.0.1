package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface MessageLikeContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends ITSListView<MessageItemBean, Presenter> {

    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository {
    }

    interface Presenter extends ITSListPresenter {


    }

}

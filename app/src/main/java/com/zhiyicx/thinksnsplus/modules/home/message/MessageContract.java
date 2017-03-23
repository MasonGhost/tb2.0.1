package com.zhiyicx.thinksnsplus.modules.home.message;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

import java.util.List;

import rx.Observable;

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

        void refreshLastClicikPostion(int position, MessageItemBean messageItemBean);

        /**
         * 更新未读消息数量
         *
         * @param message 对话信息
         */
        void refreshMessageUnreadNum(Message message);
    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository {
        /**
         * 获取对话列表信息
         *
         * @param user_id 用户 id
         * @return
         */
        Observable<BaseJson<List<MessageItemBean>>> getMessageList(int user_id);
    }

    interface Presenter extends ITSListPresenter<MessageItemBean> {
        MessageItemBean updateCommnetItemData();

        MessageItemBean updateLikeItemData();

        /**
         * 刷新是否显示底部红点
         * 刷新当条item 的未读数
         *
         * @param position                当条数据位置
         * @param currentMessageItemBean 当条数据
         * @param data                  所有数据
         */
        void refreshLastClicikPostion(int position, MessageItemBean currentMessageItemBean, List<MessageItemBean> data);
    }
}

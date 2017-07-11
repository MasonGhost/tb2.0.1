package com.zhiyicx.thinksnsplus.modules.home.message;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

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
        /**
         * 更新评论的
         *
         * @param messageItemBean
         */
        void updateCommnetItemData(MessageItemBean messageItemBean);

        /**
         * 更新喜欢的
         *
         * @param messageItemBean
         */
        void updateLikeItemData(MessageItemBean messageItemBean);

        /**
         * 更新置顶的
         *
         * @param messageItemBean
         */
        void updateReviewItemData(MessageItemBean messageItemBean);

        /**
         * 显示 右上角的加载动画
         */
        void showTopRightLoading();

        /**
         * 关闭右上角的加载动画
         */
        void closeTopRightLoading();

    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository extends UserInfoContract {
        /**
         * 获取对话列表信息
         *
         * @param user_id 用户 id
         * @return
         */
        Observable<BaseJson<List<MessageItemBean>>> getConversationList(int user_id);

        /**
         * 通过 对话 id 获取对话信息
         *
         * @param cid 对话 id
         * @return
         */
        Observable<BaseJson<MessageItemBean>> getSingleConversation(int cid);

        /**
         * 未读通知数量检查
         *
         * @return
         */
        Observable<Void> ckeckUnreadNotification();

        /**
         * 获取通知列表
         * @param notification
         * @param type
         * @param limit
         * @param offset
         * @return
         */
        Observable<Void> getNotificationList(String notification, String type, Integer limit, Integer offset);

    }

    interface Presenter extends ITSListPresenter<MessageItemBean> {
        MessageItemBean updateCommnetItemData();

        MessageItemBean updateLikeItemData();

        MessageItemBean updateReviewItemData();

//        MessageItemBean updateNoticesItemData();

        /**
         * 刷新是否显示底部红点
         * 刷新当条item 的未读数
         */
        void refreshConversationReadMessage();

        /**
         * 删除本地对话
         *
         * @param position
         */
        void deletConversation(int position);

        /**
         * 通过 对话 id 获取对话信息
         *
         * @param cid 对话 id
         * @return
         */
        void getSingleConversation(int cid);

        /**
         * 通过 key 标记消息已读
         *
         * @param key
         */
        void readMessageByKey(String key);

        /**
         * 检查当前消息记录
         */
        void handleFlushMessage();

        /**
         * ts 助手配置
         */
        String checkTShelper(long user_id);
    }
}

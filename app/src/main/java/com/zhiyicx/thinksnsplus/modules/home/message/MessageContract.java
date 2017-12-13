package com.zhiyicx.thinksnsplus.modules.home.message;

import android.os.Parcelable;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

import java.util.List;

import retrofit2.http.GET;
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
         */
        void updateCommnetItemData(MessageItemBean messageItemBean);

        /**
         * 更新喜欢的
         */
        void updateLikeItemData(MessageItemBean messageItemBean);

        /**
         * 更新置顶的
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

        BaseFragment getCureenFragment();

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
    interface Repository extends UserInfoContract {
        /**
         * 获取对话列表信息
         *
         * @param user_id 用户 id
         */
        Observable<List<MessageItemBean>> getConversationList(int user_id);

        Observable<List<MessageItemBeanV2>> getConversationListV2(int user_id);

        /**
         * 通过 对话 id 获取对话信息
         *
         * @param cid 对话 id
         */
        Observable<MessageItemBean> getSingleConversation(int cid);

        /**
         * 未读通知数量检查
         */
        Observable<Void> ckeckUnreadNotification();


        /**
         * 获取用户未读消息
         *
         * @see {https://slimkit.github.io/plus-docs/v2/core/users/unread#用户未读消息}
         */
        Observable<UnReadNotificaitonBean> getUnreadNotificationData();

        /**
         * 获取通知列表
         */
        Observable<List<TSPNotificationBean>> getNotificationList(String notification, String type, Integer limit, Integer offset);

        /**
         * 读取通知
         */
        Observable<TSPNotificationBean> getNotificationDetail(String notificationId);

        /**
         * 标记通知阅读
         */
        Observable<Object> makeNotificationReaded(String notificationId);

        /**
         * 标记所有通知阅读
         */
        Observable<Object> makeNotificationAllReaded();
    }

    interface Presenter extends ITSListPresenter<MessageItemBean> {
        MessageItemBean updateCommnetItemData();

        MessageItemBean updateLikeItemData();

        MessageItemBean updateReviewItemData();

        /**
         * 刷新是否显示底部红点
         * 刷新当条item 的未读数
         */
        void refreshConversationReadMessage();

        /**
         * 删除本地对话
         */
        void deletConversation(int position);

        /**
         * 通过 对话 id 获取对话信息
         *
         * @param cid 对话 id
         */
        void getSingleConversation(int cid);

        /**
         * 检查当前消息记录
         */
        void handleFlushMessage();


        void checkUnreadNotification();

        UnReadNotificaitonBean getUnreadNotiBean();
    }
}

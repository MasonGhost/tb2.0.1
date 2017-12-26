package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface MessageCommentContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends ITSListView<CommentedBean, Presenter> {
        /**
         *
         */
        void closeInputView();
    }

    interface Presenter extends ITSListPresenter<CommentedBean> {

        /**
         * send a comment
         *
         * @param mCurrentPostion current item position
         * @param replyToUserId   comment  to who
         * @param commentContent  comment content
         */
        void sendComment(int mCurrentPostion, long replyToUserId, String commentContent);

    }

}

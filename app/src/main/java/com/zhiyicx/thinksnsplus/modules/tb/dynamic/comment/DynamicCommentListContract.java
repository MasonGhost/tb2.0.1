package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
public interface DynamicCommentListContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicCommentBean, Presenter> {

        /**
         * 获取当前动态数据
         */
        DynamicDetailBeanV2 getCurrentDynamic();

        /**
         * 获取当前动态在列表中的位置
         *
         * @return
         */
        Bundle getArgumentsBundle();


        /**
         * 所有数据都有了，直接显示
         */
        void allDataReady();

        /**
         * 加载失败
         */
        void loadAllError();

        /**
         * 动态已经被删除了
         */
        void dynamicHasBeDeleted();

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {

    }

    interface Presenter extends ITSListPresenter<DynamicCommentBean> {


        /**
         * send a comment
         *
         * @param replyToUserId  comment  to who
         * @param commentContent comment content
         */
        void sendCommentV2(long replyToUserId, String commentContent);

        void reSendComment(DynamicCommentBean commentBean, long feed_id);

        /**
         * delete a comment
         *
         * @param comment_id      comment's id
         * @param commentPosition comment curren position
         */
        void deleteCommentV2(long comment_id, int commentPosition);

        /**
         * check current dynamic is has been deleted
         *
         * @param user_id   the dynamic is belong to
         * @param feed_mark the dynamic's feed_mark
         * @return
         */
        boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark);

        void allDataReady();

        void setNeedDynamicListRefresh(boolean needDynamicListRefresh);

    }
}

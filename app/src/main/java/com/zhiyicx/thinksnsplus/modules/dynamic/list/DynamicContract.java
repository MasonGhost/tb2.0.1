package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/21
 * @Contact master.jungle68@gmail.com
 */
public interface DynamicContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicDetailBeanV2, Presenter> {
        /**
         * get dynamic type
         *
         * @return dynamic type
         */
        String getDynamicType();

        /**
         *
         */
        void closeInputView();

        /**
         * 展示新的动态
         *
         * @param position -1 表示刷新列表 >=0 表示刷新指定位置的数据
         */
        void showNewDynamic(int position);

        void paySuccess();

        UserInfoBean getMcurrentUser();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {
    }

    interface Presenter extends ITSListPresenter<DynamicDetailBeanV2> {
        /**
         * handle like status
         *
         * @param isLiked true,do like ,or  cancle like
         * @param feed_id dynamic id
         * @param postion current item position
         */
        void handleLike(boolean isLiked, Long feed_id, int postion);

        /**
         * add viewcount
         *
         * @param feed_id
         * @param position
         */
        void handleViewCount(Long feed_id, int position);

        /**
         * resend dynamic
         *
         * @param position
         */
        void reSendDynamic(int position);

        /**
         * delete a comment
         *
         * @param dynamicBean     is that comment belong to feed
         * @param dynamicPositon  currren item dynamic position
         * @param comment_id      comment's id
         * @param commentPosition comment curren position
         */
        void deleteCommentV2(DynamicDetailBeanV2 dynamicBean, int dynamicPositon, long comment_id, int commentPosition);

        /**
         * 重发评论
         *
         * @param commentBean
         * @param feed_id
         */
        void reSendComment(DynamicCommentBean commentBean, long feed_id);

        /**
         * 删除动态
         *
         * @param dynamicBean
         * @param position
         */
        void deleteDynamic(DynamicDetailBeanV2 dynamicBean,int position);

        /**
         * send a comment
         *
         * @param mCurrentPostion current dynamic position
         * @param replyToUserId   comment  to who
         * @param commentContent  comment content
         */
        void sendCommentV2(int mCurrentPostion, long replyToUserId, String commentContent);

        /**
         * 通过 feedMark 获取当前数据的位置
         *
         * @param feedMark
         * @return
         */
        int getCurrenPosiotnInDataList(long feedMark);

        /**
         * 处理收藏逻辑
         */
        void handleCollect(DynamicDetailBeanV2 dynamicBean);

        /**
         * 动态分享
         */
        void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap);

        List<RealAdvertListBean> getBannerAdvert();
        List<RealAdvertListBean> getListAdvert();

        void checkNote(int note);

        void payNote(int dynamicPosition,int imagePosition,int note,boolean isImage);

        void followUser(UserInfoBean userInfoBean);
    }
}

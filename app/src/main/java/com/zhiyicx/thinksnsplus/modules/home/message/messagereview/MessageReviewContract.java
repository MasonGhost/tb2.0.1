package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:25
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MessageReviewContract {

    interface View extends ITSListView<TSPNotificationBean, Presenter> {
        TSPNotificationBean getCurrentComment();
    }

    interface Repository {
        Observable<List<TopDynamicCommentBean>> getReviewComment(int after);
        Observable<BaseJsonV2> approvedTopComment(String type,Long feed_id,int comment_id,int pinned_id);
        Observable<BaseJsonV2> refuseTopComment(String type,int pinned_id);
        Observable<BaseJsonV2> deleteTopComment(Long feed_id,int comment_id);

    }

    interface Presenter extends ITSListPresenter<TSPNotificationBean> {
        void approvedTopComment(String type,Long feed_id,int comment_id,int pinned_id);
        void refuseTopComment(String type,int pinned_id);
        void deleteTopComment(Long feed_id,int comment_id);
    }

}

package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface AnswerDetailsConstract {

    interface View extends ITSListView<AnswerCommentListBean, Presenter> {
        Long getAnswerId();

        void setCollect(boolean isCollected);

        void setDigg(boolean isDigged);

        AnswerInfoBean getAnswerInfo();

        int getInfoType();

        void infoMationHasBeDeleted();

        void loadAllError();

        void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> rewadslist);

        void updateAnswerHeader(AnswerInfoBean infoDetailBean,boolean isLoadMore);

        void deleteAnswer();

        void upDateFollowFansState(boolean isFollowed);
    }

    interface Presenter extends ITSListPresenter<AnswerCommentListBean> {

        void sendComment(int reply_id, String content);

        void shareInfo(Bitmap bitmap);

        void handleCollect(boolean isCollected, final long answer_id);

        void deleteComment(AnswerCommentListBean data);

        void handleLike(boolean isLiked, final long answer_id);

        void reqReWardsData(int id);

        void getAnswerDetail(long answer_id,long max_id,boolean isLoadMore);

        void deleteAnswer();

        List<RealAdvertListBean> getAdvert();

        void adoptionAnswer(long question_id,long answer_id);

        void handleFollowUser(UserInfoBean userInfoBean);
    }

}

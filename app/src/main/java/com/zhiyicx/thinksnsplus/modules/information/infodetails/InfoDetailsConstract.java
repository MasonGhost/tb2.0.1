package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoDetailsConstract {

    interface View extends ITSListView<InfoCommentListBean, Presenter> {
        Long getNewsId();

        void setCollect(boolean isCollected);

        void setDigg(boolean isDigged);

        InfoListDataBean getCurrentInfo();

        int getInfoType();

        void infoMationHasBeDeleted();

        void loadAllError();

        void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> rewadslist);

        void updateInfoHeader(InfoListDataBean infoDetailBean);

        void deleteInfo(boolean deleting, boolean success, String message);
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean> {

        void sendComment(int reply_id, String content);

        void shareInfo(Bitmap bitmap);

        void handleCollect(boolean isCollected, final String news_id);

        void deleteComment(InfoCommentListBean data);

        void handleLike(boolean isLiked, final String news_id);

        boolean isCollected();

        boolean isDiged();

        void reqReWardsData(int id);

        void getInfoDetail(String news_id);

        void deleteInfo();

        List<RealAdvertListBean> getAdvert();
    }

}

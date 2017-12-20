package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleMainContract {
    interface View extends ITSListView<CircleInfo, Presenter> {
        void updateCircleCount(int count);

        List<CircleInfo> getJoinedCircles();

        void setJoinedCircles(List<CircleInfo> circles);

        void setUserCertificationInfo(UserCertificationInfo data);
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        void getRecommendCircle();

        void dealCircleJoinOrExit(int position, CircleInfo circleInfo);

        void checkCertification();

        List<RealAdvertListBean> getCircleTopAdvert();
    }

    interface Repository extends IBaseCircleRepository {

        /**
         * 获取推荐的圈子
         *
         * @param limit 默认 20 ，数据返回条数 默认为20
         * @param offet 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
         * @param type  random 随机
         * @return
         */
        Observable<List<CircleInfo>> getRecommendCircle(int limit,int offet,String type);
    }
}

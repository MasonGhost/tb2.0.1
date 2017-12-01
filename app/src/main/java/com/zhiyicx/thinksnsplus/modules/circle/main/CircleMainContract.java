package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
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
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        void getRecommendCircle();
    }

    interface Repository extends IBaseCircleRepository {
        Observable<List<CircleInfo>> getRecommendCircle(int limit,int offet);
    }
}

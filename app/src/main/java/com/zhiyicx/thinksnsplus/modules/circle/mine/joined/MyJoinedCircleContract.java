package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public interface MyJoinedCircleContract {

    interface View extends ITSListView<CircleInfo, Presenter> {
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        void dealCircleJoinOrExit(int position, CircleInfo circleInfo);
    }

    interface Repository extends IBaseCircleRepository {
    }
}

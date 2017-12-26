package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleListContract {

    interface View extends ITSListView<CircleInfo, Presenter> {
        long getCategoryId();
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        void dealCircleJoinOrExit(int position, CircleInfo circleInfo);
    }
}

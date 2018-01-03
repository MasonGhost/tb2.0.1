package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public interface BaseCircleListContract {

    interface View extends ITSListView<CircleInfo, Presenter> {
        CircleClient.MineCircleType getMineCircleType();

        String getSearchInput();
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        void dealCircleJoinOrExit(int position, CircleInfo circleInfo);

        List<CircleSearchHistoryBean> getFirstShowHistory();

        List<CircleSearchHistoryBean> getAllSearchHistory();

        void cleaerAllSearchHistory();

        void deleteSearchHistory(CircleSearchHistoryBean circleSearchHistoryBean);
    }

}

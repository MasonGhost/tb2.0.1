package com.zhiyicx.thinksnsplus.modules.channel;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.List;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelListContract {
    interface View extends ITSListView<ChannelSubscripBean, ChannelListContract.Presenter> {
        /**
         * 获取页面类型
         *
         * @return
         */
        int getPageType();

    }

    interface Presenter extends ITSListPresenter<ChannelSubscripBean> {
        /**
         * 处理用户订阅状态
         */
        void handleChannelSubscrib();
    }

    interface Repository {
        /**
         * 获取我订阅的频道
         *
         * @return
         */
        Observable<BaseJson<List<ChannelSubscripBean>>> getMySubscribChannelList();

        /**
         * 获取所有的频道
         *
         * @return
         */
        Observable<BaseJson<List<ChannelSubscripBean>>> getAllChannelList();
    }
}

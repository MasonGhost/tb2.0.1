package com.zhiyicx.thinksnsplus.modules.channel.list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseChannelRepository;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelListContract {
    interface View extends ITSListView<GroupInfoBean, ChannelListContract.Presenter> {
        /**
         * 获取页面类型
         */
        int getPageType();

        /**
         * 刷新某个位置的订阅状态
         */
        void refreshSubscribState(int position);

        /**
         * 刷新列表的订阅状态
         */
        void refreshSubscribState();

        /**
         * 获取频道列表数据
         */
        List<ChannelSubscripBean> getChannelListData();

        List<GroupInfoBean> getGroupList();

        void gotoAllChannel();

    }

    interface Presenter extends ITSListPresenter<GroupInfoBean> {
        /**
         * 处理用户订阅状态
         */
        void handleChannelSubscrib(int position, ChannelSubscripBean channelSubscripBean);

        List<SystemConfigBean.Advert> getAdvert();

        void handleGroupJoin(int position, GroupInfoBean groupInfoBean);
    }

    interface Repository extends IBaseChannelRepository {
        /**
         * 获取我订阅的频道
         */
        Observable<BaseJson<List<ChannelSubscripBean>>> getMySubscribChannelList();


        /**
         * 获取所有的频道
         */
        Observable<BaseJson<List<ChannelSubscripBean>>> getAllChannelList();


        /**
         * 获取全部圈子列表
         */
        Observable<BaseJsonV2<List<GroupInfoBean>>> getAllGroupList(long max_id);

        /**
         * 获取用户加入的圈子
         */
        Observable<BaseJsonV2<List<GroupInfoBean>>> getUserJoinedGroupList(long max_id);
    }
}

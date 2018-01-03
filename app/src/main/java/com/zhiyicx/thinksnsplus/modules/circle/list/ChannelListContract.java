package com.zhiyicx.thinksnsplus.modules.circle.list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

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

        List<SystemConfigBean.Advert> getAdvert();

        void handleGroupJoin(int position, GroupInfoBean groupInfoBean);
    }

}

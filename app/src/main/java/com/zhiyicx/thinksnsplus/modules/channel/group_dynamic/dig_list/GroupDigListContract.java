package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.dig_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseChannelRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListContract;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public interface GroupDigListContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<FollowFansBean, Presenter> {
        void upDataFollowState(int position);

        /**
         * 获取传过来的动态数据，用来保存数据库
         *
         * @return
         */
        GroupDynamicListBean getDynamicBean();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IBaseChannelRepository {

    }

    interface Presenter extends ITSListPresenter<FollowFansBean> {
        /**
         * 关注或者取消关注
         */
        void handleFollowUser(int position, FollowFansBean followFansBean);

        void requestNetData(Long maxId, boolean isLoadMore, long feed_id);

        List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, GroupDynamicListBean dynamicBean);
    }
}

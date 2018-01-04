package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public interface GroupDigListContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicDigListBean, Presenter> {
        void upDataFollowState(int position);

        /**
         * 获取传过来的动态数据，用来保存数据库
         *
         * @return
         */
        GroupDynamicListBean getDynamicBean();
    }


    interface Presenter extends ITSListPresenter<DynamicDigListBean> {
        /**
         * 关注或者取消关注
         */
        void handleFollowUser(int position, UserInfoBean userInfoBean);

        void requestNetData(Long maxId, boolean isLoadMore, long feed_id);

        List<DynamicDigListBean> requestCacheData(Long maxId, boolean isLoadMore, GroupDynamicListBean dynamicBean);
    }
}

package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public interface DigListContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicDigListBean, Presenter> {
        void upDataFollowState(int position);

        /**
         * 获取传过来的动态数据，用来保存数据库
         *
         * @return
         */
        DynamicDetailBeanV2 getDynamicBean();
    }

    interface Presenter extends ITSListPresenter<DynamicDigListBean> {
        /**
         * 关注或者取消关注
         */
        void handleFollowUser(int position, UserInfoBean followFansBean);

        void requestNetData(Long maxId, boolean isLoadMore, long feed_id);

        List<DynamicDigListBean> requestCacheData(Long maxId, boolean isLoadMore, DynamicDetailBeanV2 dynamicBean);
    }
}

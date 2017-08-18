package com.zhiyicx.thinksnsplus.modules.diglist;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ICommentRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public interface DigListContract {

    interface View extends ITSListView<BaseListBean, DigListContract.Presenter> {
        void upDataFollowState(int position);

        /**
         * 获取传过来的动态数据，用来保存数据库
         *
         * @return
         */

        BaseListBean getDynamicBean();
    }

    interface Repository {

    }

    interface Presenter extends ITSListPresenter<BaseListBean> {
        /**
         * 关注或者取消关注
         */
        void handleFollowUser(int position, UserInfoBean followFansBean);

        void requestNetData(Long maxId, boolean isLoadMore, long id);

        List<BaseListBean> requestCacheData(Long maxId, boolean isLoadMore, String type);
    }
}

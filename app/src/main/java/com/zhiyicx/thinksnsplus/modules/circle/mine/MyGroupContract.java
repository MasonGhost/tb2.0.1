package com.zhiyicx.thinksnsplus.modules.circle.mine;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public interface MyGroupContract {

    interface View extends ITSListView<GroupInfoBean, Presenter> {
        void updateGroupJoinState(int position, GroupInfoBean groupInfoBean);
    }

    interface Presenter extends ITSListPresenter<GroupInfoBean> {
        void handleGroupJoin(int position, GroupInfoBean groupInfoBean);
    }

}

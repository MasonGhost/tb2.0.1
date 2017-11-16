package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleMainContract {
    interface View extends ITSListView<GroupInfoBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<GroupInfoBean> {
    }

    interface Repository extends IBaseChannelRepository {
    }
}

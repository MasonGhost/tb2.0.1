package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleListContract {

    interface View extends ITSListView<GroupInfoBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<GroupInfoBean> {
    }

    interface Repository extends IBaseCircleRepository {
    }
}

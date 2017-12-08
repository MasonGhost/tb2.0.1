package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MembersContract {

    interface View extends ITSListView<CircleMembers, Presenter> {
    }

    interface Presenter extends ITSListPresenter<CircleMembers> {
    }

    interface Repository extends IBaseCircleRepository {
    }
}

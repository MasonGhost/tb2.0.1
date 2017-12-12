package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MembersContract {

    interface View extends ITSListView<CircleMembers, Presenter> {
        long getCIrcleId();
    }

    interface Presenter extends ITSListPresenter<CircleMembers> {
    }

    interface Repository extends IBaseCircleRepository {
        Observable<List<CircleMembers>> getCircleMemberList(long circleId,int after,int limit,String type);
    }
}

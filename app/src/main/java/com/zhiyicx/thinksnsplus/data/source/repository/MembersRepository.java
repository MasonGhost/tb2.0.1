package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MembersRepository extends BaseCircleRepository implements MembersContract.Repository {

    @Inject
    public MembersRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    /**
     * 获取圈子成员列表
     *
     * @param limit
     * @param after
     * @param type
     * @return
     */
    @Override
    public Observable<List<CircleMembers>> getCircleMemberList(long circleId, int after, int limit, String type) {
        return mCircleClient.getCircleMemberList(circleId, limit, after, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 转让圈子
     *
     * @param circleId
     * @param userId
     * @return
     */
    @Override
    public Observable<CircleMembers> attornCircle(long circleId, long userId) {
        return mCircleClient.attornCircle(circleId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

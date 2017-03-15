package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailRepository extends BaseDynamicRepository implements DynamicDetailContract.Repository {
    private UserInfoRepository mUserInfoRepository;
    private DynamicClient mDynamicClient;
    public DynamicDetailRepository(ServiceManager serviceManager, Context context) {
        super(serviceManager, context);
        mDynamicClient=serviceManager.getDynamicClient();
        mUserInfoRepository = new UserInfoRepository(serviceManager);
    }


    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids) {
        return mUserInfoRepository.getUserFollowState(user_ids);
    }

}

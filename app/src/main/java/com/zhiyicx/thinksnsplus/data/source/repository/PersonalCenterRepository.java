package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.DYNAMIC_TYPE_USERS;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterRepository extends BaseDynamicRepository implements PersonalCenterContract.Repository {

    @Inject
    public PersonalCenterRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }


    @Override
    public Observable<List<DynamicDetailBeanV2>> getDynamicListForSomeone(Long user_id, Long max_id) {
        return getDynamicListV2(DYNAMIC_TYPE_USERS, max_id, user_id, false);
    }

    @Override
    public Observable<BaseJson<FollowFansBean>> getUserFollowState(String user_ids) {
        return mUserInfoRepository.getUserFollowState(user_ids)
                .map(listBaseJson -> {
                    BaseJson<FollowFansBean> beanBaseJson = new BaseJson<>();
                    List<FollowFansBean> followFansBeanList = listBaseJson.getData();
                    if (listBaseJson.isStatus() && followFansBeanList != null && !followFansBeanList.isEmpty()) {
                        // 肯定最多返回一条用户数据
                        beanBaseJson.setData(followFansBeanList.get(0));
                    } else {
                        beanBaseJson.setData(null);
                    }
                    beanBaseJson.setStatus(listBaseJson.isStatus());
                    beanBaseJson.setMessage(listBaseJson.getMessage());
                    beanBaseJson.setCode(listBaseJson.getCode());
                    return beanBaseJson;
                });
    }
}

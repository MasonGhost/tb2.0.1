package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

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
    public Observable<BaseJson<UserInfoBean>> getCurrentUserInfo(Long userId) {
        List<Object> integers = new ArrayList<>();
        integers.add(userId);
        // 获取用户信息，并将它进行类型转换
        return mUserInfoRepository.getUserInfo(integers)
                .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<UserInfoBean>>() {
                    @Override
                    public BaseJson<UserInfoBean> call(BaseJson<List<UserInfoBean>> listBaseJson) {
                        BaseJson<UserInfoBean> beanBaseJson = new BaseJson<UserInfoBean>();
                        List<UserInfoBean> userInfoBeanList = listBaseJson.getData();
                        if (listBaseJson.isStatus() && userInfoBeanList != null && !userInfoBeanList.isEmpty()) {
                            // 肯定最多返回一条用户数据
                            beanBaseJson.setData(userInfoBeanList.get(0));
                        } else {
                            beanBaseJson.setData(null);
                        }
                        beanBaseJson.setStatus(listBaseJson.isStatus());
                        beanBaseJson.setMessage(listBaseJson.getMessage());
                        beanBaseJson.setCode(listBaseJson.getCode());
                        return beanBaseJson;
                    }
                });
    }

    @Override
    public Observable<List<DynamicDetailBeanV2>> getDynamicListForSomeone(Long user_id, Long max_id) {
        String type = String.format(ApiConfig.DYNAMIC_TYPE_SOMEONE, user_id);
        return getDynamicListV2(type, max_id, false);
    }

    @Override
    public Observable<BaseJson<FollowFansBean>> getUserFollowState(String user_ids) {
        return mUserInfoRepository.getUserFollowState(user_ids)
                .map(new Func1<BaseJson<List<FollowFansBean>>, BaseJson<FollowFansBean>>() {
                    @Override
                    public BaseJson<FollowFansBean> call(BaseJson<List<FollowFansBean>> listBaseJson) {
                        BaseJson<FollowFansBean> beanBaseJson = new BaseJson<FollowFansBean>();
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
                    }
                });
    }
}

package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterRepository implements PersonalCenterContract.Repository {
    private UserInfoRepository mUserInfoRepository;

    public PersonalCenterRepository(ServiceManager serviceManager) {
        mUserInfoRepository = new UserInfoRepository(serviceManager);
    }

    @Override
    public Observable<BaseJson<UserInfoBean>> getCurrentUserInfo(Long userId) {
        List<Long> integers = new ArrayList<>();
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
}

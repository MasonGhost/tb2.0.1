package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class FollowFansListRepository implements FollowFansListContract.Repository {
    private FollowFansClient mFollowFansClient;

    public FollowFansListRepository(ServiceManager serviceManager) {
        mFollowFansClient = serviceManager.getFollowFansClient();
    }

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFollowFansListFromNet(int userId, boolean isFollowed) {
        return Observable.create(new Observable.OnSubscribe<BaseJson<List<FollowFansBean>>>() {
            @Override
            public void call(Subscriber<? super BaseJson<List<FollowFansBean>>> subscriber) {
                List<FollowFansBean> datas = new ArrayList<FollowFansBean>();
                for (int i = 0; i < 10; i++) {
                    FollowFansBean followFansItemBean = new FollowFansBean();
                    followFansItemBean.setFollowState(1);
                    followFansItemBean.setUserId(10000l+i);
                    followFansItemBean.setFollowedUserId(20002l+i);
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(10000l+i);
                    userInfoBean.setUserIcon("http://image.xinmin.cn/2017/01/11/bedca80cdaa44849a813e7820fff8a26.jpg");
                    userInfoBean.setName("魂行道");
                    userInfoBean.setIntro("走在风中今天阳光突然好温柔，天的温柔地的温柔像你抱着我");
                    followFansItemBean.setFllowedUser(userInfoBean);
                    datas.add(followFansItemBean);
                }
                BaseJson<List<FollowFansBean>> baseJson = new BaseJson<List<FollowFansBean>>();
                baseJson.setStatus(true);
                baseJson.setData(datas);
                baseJson.setCode(0);
                baseJson.setMessage("数据获取成功");
                subscriber.onNext(baseJson);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

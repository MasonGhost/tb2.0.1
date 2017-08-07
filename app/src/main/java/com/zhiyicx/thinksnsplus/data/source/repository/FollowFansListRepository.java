package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GsonFollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class FollowFansListRepository implements FollowFansListContract.Repository {
    private FollowFansClient mFollowFansClient;
    @Inject
    protected UserInfoRepository mUserInfoRepository;
    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public FollowFansListRepository(ServiceManager serviceManager, Application context) {
        mFollowFansClient = serviceManager.getFollowFansClient();
    }

    @Override
    public Observable<List<UserInfoBean>> getFollowListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFollowsList(userId, maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .map(userInfoBeen -> {
                    // 保存用户信息
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                    return userInfoBeen;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserInfoBean>> getFansListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFansList(userId, maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .map(userInfoBeen -> {
                    // 保存用户信息
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                    return userInfoBeen;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<Object> followUser(long userId) {
        return mFollowFansClient.followUser(userId);
    }

    @Override
    public Observable<Object> cancleFollowUser(long userId) {
        return mFollowFansClient.cancelFollowUser(userId);
    }

    /**
     * 重新封装服务器数据
     */
    private Observable<BaseJson<List<FollowFansBean>>> packageData(BaseJson<GsonFollowFansBean> gsonFollowFansBeanBaseJson, final long userId, final List<FollowFansBean> followFansBeanList) {
        if (gsonFollowFansBeanBaseJson.isStatus() && followFansBeanList != null) {
            List<Object> targetUserIds = new ArrayList<>();
            for (FollowFansBean followFansBean : followFansBeanList) {
                targetUserIds.add(followFansBean.getTargetUserId());
            }
            targetUserIds.add(AppApplication.getmCurrentLoginAuth().getUser_id());
            return mUserInfoRepository.getUserInfo(targetUserIds)
                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<FollowFansBean>>>() {
                        @Override
                        public BaseJson<List<FollowFansBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                            if (userinfobeans.isStatus()) {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                // 获取到用户信息
                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }

                                for (FollowFansBean followFansBean : followFansBeanList) {
                                    // 关注主体是当前传入的userId
                                    followFansBean.setOriginUserId(userId);
                                    followFansBean.setOrigintargetUser(null);
                                    // 存储用户信息到粉丝关注列表
                                    followFansBean.setTargetUserInfo(userInfoBeanSparseArray.get((int) followFansBean.getTargetUserId()));
                                }
                                // 保存用户信息
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                            } else {
                                // 用户信息获取失败，是否抛出错误或者异常，还是作为正常数据处理，只是界面上没有用户信息显示，这是一个问题

                            }
                            BaseJson<List<FollowFansBean>> listBaseJson = new BaseJson<List<FollowFansBean>>();
                            listBaseJson.setCode(userinfobeans.getCode());
                            listBaseJson.setMessage(userinfobeans.getMessage());
                            listBaseJson.setStatus(userinfobeans.isStatus());
                            listBaseJson.setData(followFansBeanList);
                            return listBaseJson;
                        }
                    });
        } else {
            BaseJson<List<FollowFansBean>> listBaseJson = new BaseJson<List<FollowFansBean>>();
            listBaseJson.setCode(gsonFollowFansBeanBaseJson.getCode());
            listBaseJson.setMessage(gsonFollowFansBeanBaseJson.getMessage());
            listBaseJson.setStatus(gsonFollowFansBeanBaseJson.isStatus());
            return Observable.just(listBaseJson);
        }
    }
}

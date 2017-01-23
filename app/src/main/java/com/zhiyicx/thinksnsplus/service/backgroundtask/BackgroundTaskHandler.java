package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.app.Application;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

/**
 * @Describe 后台任务处理逻辑
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskHandler {
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU = 500;// 消息发送间隔时间，防止 cpu 占用过高
    @Inject
    Application mContext;
    @Inject
    ServiceManager mServiceManager;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    BackgroundRequestTaskBeanGreenDaoImpl mBackgroundRequestTaskBeanGreenDao;
    @Inject
    AuthRepository mAuthRepository;

    private Queue<BackgroundRequestTaskBean> mTaskBeanConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();// 线程安全的队列

    private List<BackgroundRequestTaskBean> mBackgroundRequestTaskBeanCaches = new ArrayList<>();

    private boolean mIsExit = false; // 是否关闭

    public BackgroundTaskHandler() {
        init();
    }

    /**
     * 加入任务
     *
     * @param backgroundRequestTaskBean 任务
     * @return 如果任务为 null，返回 false,否者返回 true
     */
    public boolean addBackgroundRequestTask(BackgroundRequestTaskBean backgroundRequestTaskBean) {
        if (backgroundRequestTaskBean == null) {
            return false;
        }
        if (mTaskBeanConcurrentLinkedQueue.add(backgroundRequestTaskBean)) {
            mBackgroundRequestTaskBeanCaches.add(backgroundRequestTaskBean);
            return true;
        }
        return false;
    }

    /**
     * 停止处理任务
     */
    public void stopTask() {
        mIsExit = true;
    }

    private void init() {
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        getCacheData();
        new Thread(handleTaskRunnable).start();
    }

    /**
     * 获取缓存中没有被执行的数据
     */
    private void getCacheData() {
        List<BackgroundRequestTaskBean> cacheDatas = mBackgroundRequestTaskBeanGreenDao.getMultiDataFromCache();
        if (cacheDatas != null) {
            for (BackgroundRequestTaskBean tmp : cacheDatas) {
                mTaskBeanConcurrentLinkedQueue.add(tmp);
            }
        }
    }

    /**
     * 处理任务线程
     */
    private Runnable handleTaskRunnable = new Runnable() {
        @Override
        public void run() {
            while (!mIsExit) {
                if (NetUtils.netIsConnected(mContext) && !mTaskBeanConcurrentLinkedQueue.isEmpty()) {
                    BackgroundRequestTaskBean backgroundRequestTaskBean = mTaskBeanConcurrentLinkedQueue.poll();
                    handleTask(backgroundRequestTaskBean);
                }
                threadSleep();
            }
            // 存储未执行的数据到数据库，下次执行
            if (mBackgroundRequestTaskBeanCaches.size() > 0) {
                mBackgroundRequestTaskBeanGreenDao.saveMultiData(mBackgroundRequestTaskBeanCaches);
            }
        }
    };

    /**
     * 线程休息
     */
    private void threadSleep() {
        //防止cpu占用过高
        try {
            Thread.sleep(MESSAGE_SEND_INTERVAL_FOR_CPU);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理后台任务，用户信息和 IM 需要对返回数据处理，其他请求只是通知服务器，不需要做后续操作
     *
     * @param backgroundRequestTaskBean 后台任务
     */
    private void handleTask(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
            EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
            return;
        }
        backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
        switch (backgroundRequestTaskBean.getMethodType()) {
            /**
             * 通用接口处理
             */
            case POST:
                mServiceManager.getCommonClient().handleBackGroundTask(backgroundRequestTaskBean.getPath(), backgroundRequestTaskBean.getParams())
                        .subscribe(new BaseSubscribe<CacheBean>() {
                            @Override
                            protected void onSuccess(CacheBean data) {
                                mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }
                        });
                break;
            case GET:


                break;
            /**
             * 获取 IM 信息，必须保证 header 中已经加入了权限 token
             */
            case GET_IM_INFO:
                mAuthRepository.getImInfo()
                        .subscribe(new BaseSubscribe<IMBean>() {
                            @Override
                            protected void onSuccess(IMBean data) {
                                mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                                System.out.println("data = " + data.toString());
                                IMConfig imConfig = new IMConfig();
                                imConfig.setImUid(data.getUser_id());
                                imConfig.setToken(data.getIm_password());
                                imConfig.setWeb_socket_authority("ws://192.168.10.222:9900"); // TODO: 2017/1/20  服务器统一配置接口返回数据
                                ZBIMClient.getInstance().login(imConfig);
                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }
                        });

                break;
            /**
             * 获取用户信息
             */
            case GET_USER_INFO:
                if (backgroundRequestTaskBean.getParams() == null || backgroundRequestTaskBean.getParams().get("user") == null) {
                    return;
                }
                mServiceManager.getUserInfoClient().getUserInfo((Integer) backgroundRequestTaskBean.getParams().get("user"))
                        .subscribe(new BaseSubscribe<UserInfoBean>() {
                            @Override
                            protected void onSuccess(UserInfoBean data) {
                                mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                                mUserInfoBeanGreenDao.insertOrReplace(data);
                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTaskBean);
                            }
                        });
                break;

            default:
        }

    }

}

package com.zhiyicx.thinksnsplus.service;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import org.simple.eventbus.EventBus;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskManager {
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU = 100;// 消息发送间隔时间，防止 cpu 占用过高

    private static volatile BackgroundTaskManager sBackgroundTaskManager;
    private Queue<BackgroundRequestTask> mBackgroundRequestTasks = new ConcurrentLinkedQueue<>();// 线程安全的队列
    private boolean mIsExit = false; // 是否关闭

    @Inject
    ServiceManager mServiceManager;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    AuthRepository mAuthRepository;

    private BackgroundTaskManager() {
        init();
    }

    public static BackgroundTaskManager getInstance() {

        if (sBackgroundTaskManager == null) {
            synchronized (BackgroundTaskManager.class) {
                if (sBackgroundTaskManager == null) {
                    sBackgroundTaskManager = new BackgroundTaskManager();
                }
            }
        }
        return sBackgroundTaskManager;
    }

    /**
     * 加入任务
     * @param backgroundRequestTask 任务
     * @return 如果任务为 null，返回 false,否者返回 true
     */
    public boolean addBackgroundRequestTask(BackgroundRequestTask backgroundRequestTask) {
        if (backgroundRequestTask == null) {
            return false;
        }
        return mBackgroundRequestTasks.add(backgroundRequestTask);
    }

    /**
     * 停止处理任务
     */
    public void stopTask() {
        mIsExit = true;
    }

    private void init() {
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        new Thread(handleTaskRunnable).start();
    }

    /**
     * 处理任务线程
     */
    private Runnable handleTaskRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsExit) {
                if (!mBackgroundRequestTasks.isEmpty()) {
                    BackgroundRequestTask backgroundRequestTask = mBackgroundRequestTasks.poll();
                    handleTask(backgroundRequestTask);
                }
                threadSleep();
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
     * @param backgroundRequestTask 后台任务
     */
    private void handleTask(final BackgroundRequestTask backgroundRequestTask) {
        if (backgroundRequestTask.getMax_retry_count() - 1 <= 0) {
            EventBus.getDefault().post(backgroundRequestTask, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
            return;
        }
        backgroundRequestTask.setMax_retry_count(backgroundRequestTask.getMax_retry_count() - 1);
        switch (backgroundRequestTask.getMethodType()) {
            /**
             * 通用接口处理
             */
            case POST:
                mServiceManager.getCommonClient().handleTask(backgroundRequestTask.getPath(), backgroundRequestTask.getParams())
                        .subscribe(new BaseSubscribe<CacheBean>() {
                            @Override
                            protected void onSuccess(CacheBean data) {

                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTask);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTask);
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
                                System.out.println("data = " + data.toString());
                                IMConfig imConfig = new IMConfig();
                                imConfig.setImUid(data.getUser_id());
                                imConfig.setToken(data.getIm_password());
                                imConfig.setWeb_socket_authority("ws://192.168.10.222:9900"); // TODO: 2017/1/20  服务器统一配置接口返回数据
                                ZBIMClient.getInstance().login(imConfig);
                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTask);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTask);
                            }
                        });

                break;
            /**
             * 获取用户信息
             */
            case GET_USER_INFO:
                if (backgroundRequestTask.getParams() == null || backgroundRequestTask.getParams().get("user_id") == null) {
                    return;
                }
                mServiceManager.getUserInfoClient().getUserInfo((Integer) backgroundRequestTask.getParams().get("user_id"))
                        .subscribe(new BaseSubscribe<UserInfoBean>() {
                            @Override
                            protected void onSuccess(UserInfoBean data) {
                                mUserInfoBeanGreenDao.insertOrReplace(data);
                            }

                            @Override
                            protected void onFailure(String message) {
                                addBackgroundRequestTask(backgroundRequestTask);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                addBackgroundRequestTask(backgroundRequestTask);
                            }
                        });
                break;

            default:
        }

    }

}

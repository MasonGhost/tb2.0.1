package com.zhiyicx.thinksnsplus.service;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppComponent;
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

    public BackgroundTaskManager() {
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

    public boolean addBackgroundRequestTask(BackgroundRequestTask backgroundRequestTask) {
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
     * 处理后台任务
     *
     * @param backgroundRequestTask
     */
    private void handleTask(final BackgroundRequestTask backgroundRequestTask) {
        if (backgroundRequestTask.getMax_retry_count() - 1 <= 0) {
            EventBus.getDefault().post(backgroundRequestTask, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
        }
        backgroundRequestTask.setMax_retry_count(backgroundRequestTask.getMax_retry_count() - 1);
        AppComponent appComponent = AppApplication.AppComponentHolder.getAppComponent();
        switch (backgroundRequestTask.getMethodType()) {
            case POST:
                mServiceManager.getCommonClient().handleTask(backgroundRequestTask.getPath(), backgroundRequestTask.getParams())
                        .subscribe(new BaseSubscribe<CacheBean>() {
                            @Override
                            protected void onSuccess(CacheBean data) {

                            }

                            @Override
                            protected void onFailure(String message) {

                            }

                            @Override
                            protected void onException(Throwable throwable) {

                            }
                        });
                break;
            case GET:


                break;

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

            case GET_USER_INFO:
                mServiceManager.getUserInfoClient().getUserInfo("")
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

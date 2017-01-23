package com.zhiyicx.thinksnsplus.modules;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/16
 * @contact email:450127106@qq.com
 */

public class RxUnitTestTools {

    /**
     * 把异步变成同步，方便测试
     */
    public static void openRxTools() {
        Func1<Scheduler, Scheduler> schedulerFunc = new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        };

        RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        };

        RxJavaHooks.reset();
        RxJavaHooks.setOnIOScheduler(schedulerFunc);
        RxJavaHooks.setOnComputationScheduler(schedulerFunc);

        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);
    }

    /**
     * 回复异步
     */
    public static void closeRxTools(){
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }
}

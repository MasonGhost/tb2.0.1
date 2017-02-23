<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsContract;

import rx.Observable;
import rx.Subscriber;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class SettingsRepository implements SettingsContract.Repository {

    public SettingsRepository(ServiceManager serviceManager) {
    }

    /**
     *  当前只计算了系统缓存文件夹的大小
     * @param context
     * @return
     */
    @Override
    public Observable<String> getDirCacheSize(final Context context) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String dirSize = FileUtils.getDirSize(FileUtils.getCacheFile(context));
                    subscriber.onNext(dirSize);//将数据传给观察者
                    subscriber.onCompleted();//通知观察者完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> cleanCache(final Context context) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean isDelete = FileUtils.deleteDir(FileUtils.getCacheFile(context));
                    subscriber.onNext(isDelete);//将数据传给观察者
                    subscriber.onCompleted();//完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
=======
package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsContract;

import rx.Observable;
import rx.Subscriber;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class SettingsRepository implements SettingsContract.Repository {

    public SettingsRepository(ServiceManager serviceManager) {
    }

    /**
     *  当前只计算了系统缓存文件夹的大小
     * @param context
     * @return
     */
    @Override
    public Observable<String> getDirCacheSize(final Context context) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String dirSize = FileUtils.getDirSize(FileUtils.getCacheFile(context));
                    subscriber.onNext(dirSize);//将数据传给观察者
                    subscriber.onCompleted();//通知观察者完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> cleanCache(final Context context) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean isDelete = FileUtils.deleteDir(FileUtils.getCacheFile(context));
                    subscriber.onNext(isDelete);//将数据传给观察者
                    subscriber.onCompleted();//完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94

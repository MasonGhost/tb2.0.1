package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.guide.GuideContract;

import rx.Observable;
import rx.Subscriber;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class GuideRepository implements GuideContract.Repository {

    public GuideRepository(ServiceManager serviceManager) {
    }

    /**
     * 当前只计算了系统缓存文件夹的大小
     *
     * @param context
     * @return
     */
    @Override
    public Observable<String> getDirCacheSize(final Context context) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String dirSize = FileUtils.getDirSize(FileUtils.getCacheFile(context,false));
                    subscriber.onNext(dirSize);//将数据传给观察者
                    subscriber.onCompleted();//通知观察者完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }


}

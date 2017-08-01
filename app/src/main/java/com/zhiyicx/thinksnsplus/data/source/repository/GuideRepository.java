package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.LaunchAdvertBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.guide.GuideContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class GuideRepository implements GuideContract.Repository {

    private CommonClient mCommonClient;

    @Inject
    public GuideRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
    }

    /**
     * 当前只计算了系统缓存文件夹的大小
     *
     * @param context
     * @return
     */
    @Override
    public Observable<String> getDirCacheSize(final Context context) {
        return Observable.create(subscriber -> {
            try {
                String dirSize = FileUtils.getDirSize(FileUtils.getCacheFile(context, false));
                subscriber.onNext(dirSize);//将数据传给观察者
                subscriber.onCompleted();//通知观察者完成
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<AllAdverListBean>> getLaunchAdverts() {
        return mCommonClient.getLaunchAdvert()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<RealAdvertListBean>> getRealAdverts(long space_id) {
        return mCommonClient.getRealAdvert(space_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

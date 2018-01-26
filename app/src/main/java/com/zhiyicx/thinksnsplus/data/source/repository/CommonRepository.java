package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UpdateInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ICommonRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 通用接口数据处理
 * @Author Jungle68
 * @Date 2017/12/25
 * @Contact master.jungle68@gmail.com
 */
public class CommonRepository implements ICommonRepository {

    private CommonClient mCommonClient;
    @Inject
    Application mContext;

    @Inject
    public CommonRepository(ServiceManager serviceManager) {
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

    @Override
    public Observable<List<RealAdvertListBean>> getAllRealAdverts(List<Object> space_id) {
        String space_ids = space_id.toString();
        space_ids = space_ids.replace("[", "");
        space_ids = space_ids.replace("]", "");
        return mCommonClient.getAllRealAdvert(space_ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 当前只计算了系统缓存文件夹的大小
     *
     * @return
     */
    @Override
    public Observable<String> getDirCacheSize() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String dirSize = FileUtils.getDirSizeUnit(FileUtils.getCacheFile(mContext, false));

                    subscriber.onNext(dirSize);//将数据传给观察者
                    subscriber.onCompleted();//通知观察者完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 清理缓存
     *
     * @return
     */
    @Override
    public Observable<Boolean> cleanCache() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean isDelete = FileUtils.deleteDir(FileUtils.getCacheFile(mContext, false));
                    subscriber.onNext(isDelete);//将数据传给观察者
                    subscriber.onCompleted();//完成
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 检查更新
     *
     * @return
     */
    @Override
    public Observable<UpdateInfoBean> checkUpdate() {
        return null;
    }
}

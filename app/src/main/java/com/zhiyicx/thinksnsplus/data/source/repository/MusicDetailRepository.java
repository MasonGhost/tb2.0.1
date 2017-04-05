package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicDetailRepository implements MusicDetailContract.Repository {
    private MusicClient mMusicClient;
    private Context mContext;

    @Inject
    public MusicDetailRepository(ServiceManager serviceManager, Application application) {
        mMusicClient = serviceManager.getMusicClient();
        mContext = application;
    }

    @Override
    public Observable<BaseJson<MusicAlbumDetailsBean>> getMusicAblum(String id) {
        return mMusicClient.getMusicAblum(id);
    }

    @Override
    public Observable<BaseJson<MusicDetaisBean>> getMusicDetails(String music_id) {
        return mMusicClient.getMusicDetails(music_id);
    }

    @Override
    public void handleCollect(boolean isCollected, final String special_id) {
        Observable.just(isCollected)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("special_id", special_id);
                        // 后台处理
                        if (aBoolean) {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                    (BackgroundTaskRequestMethodConfig.POST, params);
                        } else {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                    (BackgroundTaskRequestMethodConfig.DELETE, params);
                        }
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_INFO_COLLECT_FORMAT, special_id));
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                                (backgroundRequestTaskBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void shareAblum(String special_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("special_id", special_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.PATCH, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                .APP_PATH_MUSIC_ABLUM_SHARE, special_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }
}

package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.data.source.remote.LiveClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ILiveRepository;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.MAX_RETRY_COUNTS;
import static com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.RETRY_DELAY_TIME;

/**
 * @Describe 直播相关
 * @Author Jungle68
 * @Date 2017/11/23
 * @Contact master.jungle68@gmail.com
 */
public class LiveRepository implements ILiveRepository {

    private LiveClient mLiveClient;
    @Inject
    Application mContext;


    @Inject
    public LiveRepository(ServiceManager serviceManager) {
        mLiveClient = serviceManager.getLiveClient();
    }

    /**
     * 获取票据
     *
     * @return
     */
    @Override
    public Observable<String> getLiveTicket() {
        return mLiveClient.getLiveTicket()
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

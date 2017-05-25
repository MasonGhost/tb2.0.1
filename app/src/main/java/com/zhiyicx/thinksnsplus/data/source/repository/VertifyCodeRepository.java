package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/25
 * @Contact master.jungle68@gmail.com
 */

public class VertifyCodeRepository implements IVertifyCodeRepository {
    protected CommonClient mCommonClient;
    protected Context mContext;

    @Inject
    public VertifyCodeRepository(ServiceManager serviceManager, Application context) {
        this.mCommonClient = serviceManager.getCommonClient();
        this.mContext=context;
    }

    @Override
    public Observable<Object> getMemberVertifyCode(String phone) {
        return mCommonClient.getMemberVertifyCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> getNonMemberVertifyCode(String phone) {
        return mCommonClient.getNonMemberVertifyCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

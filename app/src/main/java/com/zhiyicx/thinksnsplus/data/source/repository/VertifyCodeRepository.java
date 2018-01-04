package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IVertifyCodeRepository;

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

    @Inject
    public VertifyCodeRepository(ServiceManager serviceManager) {
        this.mCommonClient = serviceManager.getCommonClient();
    }

    @Override
    public Observable<Object> getMemberVertifyCode(String phone) {
        return mCommonClient.getMemberVertifyCode(phone, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> getMemberVerifyCodeByEmail(String email) {
        return mCommonClient.getMemberVertifyCode(null, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> getNonMemberVertifyCode(String phone) {
        return mCommonClient.getNonMemberVertifyCode(phone, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> getNonMemberVerifyCodeByEmail(String email) {
        return mCommonClient.getNonMemberVertifyCode(null, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

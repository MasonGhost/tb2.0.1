package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailContract;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class CertificationDetailRepository implements CertificationDetailContract.Repository {

    private UserInfoClient mUserInfoClient;

    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;

    @Inject
    public CertificationDetailRepository(ServiceManager manager) {
        mUserInfoClient = manager.getUserInfoClient();
    }

    @Override
    public Observable<UserCertificationInfo> getCertificationInfo() {
        return mUserInfoClient.getUserCertificationInfo();
    }

    public void saveCertificationInfo() {
        mUserInfoClient.getUserCertificationInfo()
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {
                    @Override
                    protected void onSuccess(UserCertificationInfo data) {
                        mUserCertificationInfoDao.saveSingleData(data);
                    }
                });
    }
}

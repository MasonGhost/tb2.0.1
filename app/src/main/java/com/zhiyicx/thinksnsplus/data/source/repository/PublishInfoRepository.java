package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoRepository implements PublishInfoContract.Repository {
    private InfoMainClient mInfoMainClient;

    @Inject
    public PublishInfoRepository(ServiceManager serviceManager) {
        mInfoMainClient=serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJsonV2<Object>> publishInfo(InfoPublishBean infoPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(infoPublishBean));
        return mInfoMainClient.publishInfo(infoPublishBean.getCategoryId(),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

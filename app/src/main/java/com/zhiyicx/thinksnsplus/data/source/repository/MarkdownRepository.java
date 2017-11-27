package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/08/07/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownRepository extends BaseCircleRepository implements MarkdownContract.Repository {
    private InfoMainClient mInfoMainClient;

    @Inject
    public MarkdownRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJsonV2<Object>> publishInfo(InfoPublishBean infoPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(infoPublishBean));
        return mInfoMainClient.publishInfo(infoPublishBean.getCategoryId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> updateInfo(InfoPublishBean infoPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(infoPublishBean));
        return mInfoMainClient.updateInfo(infoPublishBean.getCategoryId(), infoPublishBean.getNews_id(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

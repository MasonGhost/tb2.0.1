package com.zhiyicx.zhibolibrary.model.impl;

import com.zhiyicx.zhibolibrary.model.HomeModel;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/28.
 */
public class HomeModelImpl implements HomeModel {
    private CommonService mService;

    public HomeModelImpl(ServiceManager manager) {
        this.mService = manager.getCommonService();//初始化请求服务
    }


    @Override
    public Observable<ResponseBody> downloadFile(String fileUrl) {
        return mService.downloadFile(fileUrl);
    }
}

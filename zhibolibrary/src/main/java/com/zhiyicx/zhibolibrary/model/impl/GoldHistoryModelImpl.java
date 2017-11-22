package com.zhiyicx.zhibolibrary.model.impl;

import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.GoldHistoryModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.GoldService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;

import okhttp3.FormBody;
import rx.Observable;

/**
 * Created by jess on 16/4/26.
 */
public class GoldHistoryModelImpl implements GoldHistoryModel {
    public static final int PAGESIZE=20;
    private GoldService mService;

    public GoldHistoryModelImpl(ServiceManager manager) {
        this.mService = manager.getGoldService();
    }

    @Override
    public Observable<BaseJson<GoldHistoryJson[]>> getGoldList(String type, int page) {
        FormBody.Builder builder = new FormBody.Builder();//form表单提交
        builder.add("api", ZBLApi.API_GET_TRADE_LIST);
        builder.add("type", type);
        builder.add("limit", PAGESIZE+"");
        builder.add("page", page + "");
        PermissionData[] permissionDatas = ZhiboApplication.getPermissionDatas();
        for (PermissionData data : permissionDatas) {
            builder.add(data.auth_key, data.auth_value);
        }

        FormBody formBody = builder.build();
        return mService.getGoldList(ZBLApi.CONFIG_BASE_DOMAIN, formBody);
    }
}

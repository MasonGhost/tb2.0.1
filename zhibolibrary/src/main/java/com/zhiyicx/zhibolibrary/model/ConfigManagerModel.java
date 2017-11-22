package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;

import rx.Observable;

/**
 * Created by jess on 16/4/23.
 */
public interface ConfigManagerModel extends UserInfoModel{
    /**
     * 通过uisd获取用户信息
     * @param ticket
     * @return
     */
    Observable<BaseJson<PermissionData[]>> getPermissionData(String ticket);
}

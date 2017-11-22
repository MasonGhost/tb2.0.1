package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import rx.Observable;

/**
 * Created by jess on 16/4/23.
 */
public interface UserInfoModel {
    /**
     * 通过uisd获取用户信息
     * @param usid
     * @param filed
     * @return
     */
    Observable<BaseJson<UserInfo[]>> getUsidInfo(final String usid, String filed);
}

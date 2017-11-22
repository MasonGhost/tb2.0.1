package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;

import rx.Observable;

/**
 * Created by jess on 16/4/23.
 */
public interface UserHomeModel extends UserInfoModel{
    Observable<BaseJson<FollowInfo>> followUser(String action,
                                                String userId);
}

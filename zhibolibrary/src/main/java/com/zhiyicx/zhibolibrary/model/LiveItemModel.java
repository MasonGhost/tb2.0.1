package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import java.util.Map;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public interface LiveItemModel {
    Observable<ApiList> getNotList(
            String order,
            String videoOrder,
            int page);

    Observable<ApiList> getFlterList(
            String order,
            String videoOrder,
            int page, Map<String, Object> condition);


    Observable<ApiList> getUserList(
            int page,
            String userId);

     Observable<BaseJson<UserInfo[]>> getUsidInfo(final String userId, String field);
    Observable<BaseJson<SearchResult[]>> getUserFollowList(String accessKey, String secretKey, String userId, String type, int page);
}

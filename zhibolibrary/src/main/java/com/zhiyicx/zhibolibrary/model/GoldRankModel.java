package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import rx.Observable;

/**
 * Created by jess on 16/4/24.
 */
public interface GoldRankModel {
    /**
     * 获取主播排行榜
     * @param usid
     * @param page
     * @return
     */
    Observable<BaseJson<SearchResult[]>> getRanking(
            String usid,
            int page);

    /**
     * 通过uisd获取用户信息
     * @param userId
     * @param field
     * @return
     */
    Observable<BaseJson<UserInfo[]>> getUsidInfo(final String userId, String field);
}

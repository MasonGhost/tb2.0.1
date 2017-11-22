package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;

import rx.Observable;

/**
 * Created by jess on 16/4/24.
 */
public interface GoldRankModel extends UserInfoModel {
    /**
     * 获取主播排行榜
     *
     * @param usid
     * @param page
     * @return
     */
    Observable<BaseJson<SearchResult[]>> getRanking(
            String usid,
            int page);

}

package com.zhiyicx.zhibolibrary.model;

import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;

import rx.Observable;

/**
 * Created by jess on 16/4/26.
 */
public interface GoldHistoryModel {
    Observable<BaseJson<GoldHistoryJson[]>> getGoldList(String type,
                                                        int page);
}

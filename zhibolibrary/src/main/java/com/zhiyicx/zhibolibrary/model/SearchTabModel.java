package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchJson;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public interface SearchTabModel {
    Observable<SearchJson> Search(String accessKey,
                                  String secretKey,
                                  String keyword,
                                  int page
    );

    Observable<BaseJson<UserInfo[]>> getUsidInfo(final String userId, String field);
}

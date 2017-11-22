package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.SearchJson;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public interface SearchTabModel extends UserInfoModel{
    Observable<SearchJson> Search(String accessKey,
                                  String secretKey,
                                  String keyword,
                                  int page
    );


}

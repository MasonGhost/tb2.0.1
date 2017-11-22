package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/5.
 */
public interface EndStreamModel extends UserHomeModel{
    Observable<ApiList> getList(String accesskey,
                                String secretkey,
                                String order,
                                int page);
}

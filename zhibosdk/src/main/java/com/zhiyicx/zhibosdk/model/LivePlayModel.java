package com.zhiyicx.zhibosdk.model;


import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public interface LivePlayModel {
    Observable<ZBApiPlay> getPlayUrl(String ak,
                                     String uid,
                                     String id);

    Observable<ZBApiPlay> getVideoUrl(String ak,
                                      String vid);

    Observable<ZBBaseJson<String>> sendGift( String ak,
                                           String stream_id,
                                           String gift_code, String count);

    Observable<ZBBaseJson<String>> sendZan(String ak,
                                          String stream_id,
                                          String count);

}

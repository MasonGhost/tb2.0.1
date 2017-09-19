package com.zhiyicx.zhibolibrary.model;


import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public interface LivePlayModel extends  UserHomeModel{
    Observable<ResponseBody> downloadFile(String fileUrl);

    /**
     * 通过usid获取用户信息
     * @param user_id
     * @param file
     * @param accessKey
     * @param secretKey
     * @return
     */
    Observable<BaseJson<UserInfo[]>> getUsidInfo(String user_id, String file,
                                                 String accessKey,
                                                 String secretKey);
}

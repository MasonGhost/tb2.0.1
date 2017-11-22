package com.zhiyicx.zhibolibrary.model;


import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public interface LivePlayModel extends  UserHomeModel{
    Observable<ResponseBody> downloadFile(String fileUrl);


}

package com.zhiyicx.zhibolibrary.model;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/28.
 */
public interface HomeModel {

    Observable<ResponseBody> downloadFile(String fileUrl);
}

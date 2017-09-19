package com.zhiyicx.zhibosdk.model;

import android.graphics.Bitmap;

import com.zhiyicx.zhibosdk.model.entity.ZBApiJson;
import com.zhiyicx.zhibosdk.model.entity.ZBApiStream;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBCheckStreamPullJson;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBIconInfo;

import java.io.File;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public interface PublishModel {
    /**
     * 创建新的流信息
     */
    Observable<ZBApiStream> createStream(String ak);

    /**
     * 效验流信息
     */
    Observable<ZBCheckStreamPullJson> checkStream(String ak,
                                                  String id);

    /**
     * 开始推流准备
     */
    Observable<ZBApiJson> startStream(String ak,

                                      String title,
                                      String location);


    /**
     * 结束推流
     */

    Observable<ZBEndStreamJson> endStream(String ak,
                                          String id);

    /**
     * 上传文件
     */
    Observable<ZBBaseJson<ZBIconInfo[]>> upload(String ak,
                                                String thumb,
                                                File icon);

    /**
     * 压缩bitmap到指定地址
     *
     * @param file
     * @param resizeBmp
     */
    boolean compressBitmap(File file, Bitmap resizeBmp);
    /**
     * 禁言
     *
     * @param usid
     * @param cid
     * @param time
     * @param ak
     * @return
     */
    Observable<ZBApiJson> imDisable(String usid, int cid, long time, String ak
    );

    /**
     * 解除禁言
     *
     * @param usid
     * @param cid
     * @param ak
     * @return
     */
    Observable<ZBApiJson> imEnable(String usid, int cid, String ak);


}

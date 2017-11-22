package com.zhiyicx.zhibolibrary.model;

import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;

import java.util.Map;

import rx.Observable;

/**
 * Created by jess on 16/5/11.
 */
public interface PublishCoreModel extends UserInfoModel {
    /**
     * IM发文本消息
     *
     * @param text
     */
    void sendTextMsg(String text, boolean presenter);


    /**
     * 发送礼物消息
     * @param jsonstr
     * @param gift_code
     * @param count
     * @param l
     */
    void sendGiftMessage(Map jsonstr, String gift_code, String count, final OnCommonCallbackListener l);

    /**
     * 发送赞消息
     * @param type
     */
    void sendZan(int type);

    /**
     * 发送关注消息
     */
    void sendAttention();
    /**
     * 获取礼物排行榜
     * @param usid
     * @param page
     * @return
     */
    Observable<BaseJson<SearchResult[]>> getGiftRank(String usid, int page);
}

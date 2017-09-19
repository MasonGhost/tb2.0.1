package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ZBApiConfig implements Serializable {
    public String zhibo_domain;//配置域名

    public List<ZBGift> gift_list;//礼物配置
    public ZBApiFilter filter_list;//直播过滤配置
    public List<ZBWebIM>  webim;//IM服务器配置
    public List<ZBNotice>  stream_notice;//直播公告配置
    public ZBFilterWordConfig filter_word_conf;//敏感词配置

    @Override
    public String toString() {
        return "ZBApiConfig{" +
                "zhibo_domain='" + zhibo_domain + '\'' +
                ", gift_list=" + gift_list +
                ", filter_list=" + filter_list +
                ", webim=" + webim +
                ", stream_notice=" + stream_notice +
                ", filter_word_conf=" + filter_word_conf +
                '}';
    }
}

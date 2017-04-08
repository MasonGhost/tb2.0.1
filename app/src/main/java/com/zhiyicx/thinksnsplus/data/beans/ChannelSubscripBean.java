package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @author LiuChao
 * @describe 对某个频道的订阅状态
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelSubscripBean {
    private long userId;// 和该频道产生联系的用户
    private boolean channelSubscriped;// 频道是否被订阅
    private long id;// 频道id

    private ChannelInfoBean mChannelInfoBean;// 该频道的信息

}

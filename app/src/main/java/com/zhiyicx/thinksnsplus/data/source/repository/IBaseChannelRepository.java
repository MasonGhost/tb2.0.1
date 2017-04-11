package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import java.util.List;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface IBaseChannelRepository {

    /**
     * 处理订阅状态
     *
     * @param channelSubscripBean
     */

    void handleSubscribChannel(ChannelSubscripBean channelSubscripBean);

    /**
     * 获取频道列表
     *
     * @param type   type 频道类型 “”表示所有的频道  “my”表示我关注的频道 在APiConfig中定义了这两个常量
     * @param userId 请求频道列表的用户,作为拓展字段如果有其他用户需要请求频道列表，使用该字段
     * @return
     */
    Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type, long userId);

    /**
     * 获取频道的动态列表
     *
     * @return
     */
    Observable<BaseJson<List<DynamicBean>>> getDynamicListFromChannel(long channel_id,long max_id);

}

package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;

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
     * 取消频道订阅
     *
     * @param channel_id
     * @return
     */
    Observable<BaseJson<Object>> cancleSubscribChannel(long channel_id);

    /**
     * 订阅频道
     *
     * @param channel_id
     * @return
     */
    Observable<BaseJson<Object>> subscribChannel(long channel_id);

    /**
     * 获取频道列表
     *
     * @param type type 频道类型 “”表示所有的频道  “my”表示我关注的频道 在APiConfig中定义了这两个常量
     * @return
     */
    Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type);
}

package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface IBaseChannelRepository extends IDynamicReppsitory {

    /**
     * 在server处理订阅状态
     */

    void handleSubscribChannel(ChannelSubscripBean channelSubscripBean);

    /**
     * 在fragment中处理订阅状态
     */
    Observable<BaseJson<Object>> handleSubscribChannelByFragment(ChannelSubscripBean channelSubscripBean);

    /**
     * 获取频道列表
     *
     * @param type   type 频道类型 “”表示所有的频道  “my”表示我关注的频道 在APiConfig中定义了这两个常量
     * @param userId 请求频道列表的用户,作为拓展字段如果有其他用户需要请求频道列表，使用该字段
     */
    Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type, long userId);

    /**
     * 获取频道的动态列表
     */
    Observable<BaseJson<List<DynamicBean>>> getDynamicListFromChannel(long channel_id, long max_id);

    Observable<List<DynamicDetailBeanV2>> getDynamicListFromChannelV2(long channel_id, long max_id);

    /**
     * 获取圈子列表
     *
     * @param type 0-全部 1-加入的
     */
    Observable<List<GroupInfoBean>> getGroupList(int type, long max_id);

    void handleGroupJoin(GroupInfoBean groupInfoBean);

    Observable<BaseJsonV2<Object>> handleGroupJoinByFragment(GroupInfoBean groupInfoBean);

}

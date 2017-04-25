package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ComponentConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentStatusBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 认证相关接口
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public interface ISystemRepository {

    /**
     * 获取扩展组件状态
     *
     * @return
     */
    ComponentStatusBean getComponentStatusLocal();

    /**
     * 保存扩张组件状态
     *
     * @param componentStatusBean
     * @return
     */
    boolean saveComponentStatus(ComponentStatusBean componentStatusBean);

    /**
     * 获取扩展组件配置
     *
     * @return
     */
    List<ComponentConfigBean> getComponentConfigLocal();

    /**
     * 保存扩展组件
     *
     * @param componentConfigBeens
     * @return
     */

    boolean saveComponentConfig(List<ComponentConfigBean> componentConfigBeens);

    /**
     * 从服务器获取组件状态
     */
    void getComponentStatusFromServer();

    /**
     * 从服务器获取组件配置
     *
     * @param component
     */
    void getComponentConfigFromServer(String component);


    /**
     * 意见反馈
     *
     * @param content 反馈内容
     * @return
     */
    Observable<BaseJson<CacheBean>> systemFeedback(String content);

    /**
     * 获取系统会话列表
     *
     * @param max_id
     * @param limit
     * @return
     */
    Observable<BaseJson<List<SystemConversationBean>>> getSystemConversations(long max_id, int limit);
}

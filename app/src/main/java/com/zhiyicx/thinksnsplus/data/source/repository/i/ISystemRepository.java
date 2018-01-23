package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;

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
     * 去获取服务器启动信息
     */
    void getBootstrappersInfoFromServer();

    /**
     * 获取本地启动信息
     */
    SystemConfigBean getBootstrappersInfoFromLocal();

    /**
     * ts 助手配置
     */
    String checkTShelper(long user_id);

    /**
     * 意见反馈
     *
     * @param content 反馈内容
     * @return
     */
    Observable<Object> systemFeedback(String content, long system_mark);

    /**
     * 获取系统会话列表
     *
     * @param max_id
     * @param limit
     * @return
     */
    Observable<List<SystemConversationBean>> getSystemConversations(long max_id, int limit);

    /**
     * 获取本地系统会话列表
     *
     * @param max_Id
     * @return
     */
    List<SystemConversationBean> requestCacheData(long max_Id);

    /**
     * 创建ts helper 对话
     */
    void creatTsHelperConversation();

    /**
     * 获取全部标签
     *
     * @return
     */
    Observable<List<TagCategoryBean>> getAllTags();

    /**
     * 搜索位置
     *
     * @param name search content
     * @return
     */
    Observable<List<LocationContainerBean>> searchLocation(String name);

    /**
     * 热门城市
     *
     * @return
     */
    Observable<List<String>> getHoCity();

    /**
     * 版本更新数据
     *
     * @return
     */
    Observable<List<AppVersionBean>> getAppNewVersion();
}

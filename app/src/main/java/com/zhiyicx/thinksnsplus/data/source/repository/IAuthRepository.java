package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentStatusBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 认证相关接口
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public interface IAuthRepository {
    /**
     * 保存登录后获取到的认证信息
     *
     * @param authBean {@link AuthBean}认证信息类
     * @return
     */
    boolean saveAuthBean(AuthBean authBean);

    /**
     * 获取保存登录后获取到的认证信息
     *
     * @return
     */
    AuthBean getAuthBean();


    /**
     * 注册
     *
     * @return
     */
    Observable<BaseJson<IMBean>> getImInfo();

    /**
     * 刷新 Token
     */
    void refreshToken();

    /**
     * 删除认证信息
     *
     * @return
     */
    boolean clearAuthBean();

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return
     */
    boolean isLogin();

    /**
     * 保存IM 登录配置信息
     * @param imConfig 配置信息
     * @return true,保存成功
     */
    boolean saveIMConfig(IMConfig imConfig);

    /**
     * 获取 IM 配置信息
     * @return 配置信息
     */
    IMConfig getIMConfig();

    void loginIM();

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


    void getComponentStatusFromServer();

    void getComponentConfigFromServer(String component);

}

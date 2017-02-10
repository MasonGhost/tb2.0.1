package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;

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
}

package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;

import retrofit2.Call;
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
    Observable<IMBean> getImInfo();

    /**
     * 刷新 Token
     */
    void refreshToken();

    /**
     * 同步刷新 token
     */
    Call<AuthBean> refreshTokenSyn();

    /**
     * 删除认证信息
     *
     * @return
     */
    boolean clearAuthBean();

    /**
     * 清除三方认证信息
     */
    void clearThridAuth();

    void clearThridAuth(SHARE_MEDIA share_media);

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return true  ,  is logined
     */
    boolean isLogin();

    /**
     * 是否是游客
     *
     * @return true the current login user is tourist
     */
    boolean isTourist();

    /**
     * 保存IM 登录配置信息
     *
     * @param imConfig 配置信息
     * @return true, 保存成功
     */
    boolean saveIMConfig(IMConfig imConfig);

    /**
     * 获取 IM 配置信息
     *
     * @return 配置信息
     */
    IMConfig getIMConfig();

    void loginIM();

    /**
     * token 是否过期
     *
     * @return
     */
    boolean isNeededRefreshToken();
}

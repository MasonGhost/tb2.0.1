package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;

/**
 * @Describe 认证相关接口
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public interface IAuthRepository {
    /**
     * 保存登录后获取到的认证信息
     * @param authBean {@link AuthBean}认证信息类
     * @return
     */
    boolean  saveAuthBean(AuthBean authBean);
}

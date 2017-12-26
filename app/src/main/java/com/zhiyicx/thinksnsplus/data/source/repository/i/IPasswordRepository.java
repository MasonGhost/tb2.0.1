package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.baseproject.cache.CacheBean;

import rx.Observable;

/**
 * @Describe 密码相关
 * @Author Jungle68
 * @Date 2017/12/25
 * @Contact master.jungle68@gmail.com
 */
public interface IPasswordRepository extends IVertifyCodeRepository {

    /**
     * 找回密码
     *
     * @param phone       电话号码
     * @param vertifyCode 验证码
     * @param newPassword 新密码
     * @return
     */
    Observable<CacheBean> findPasswordV2(String phone
            , String vertifyCode, String newPassword);

    /**
     * 找回密码
     *
     * @param email       邮箱地址
     * @param verifyCode  验证码
     * @param newPassword 新密码
     * @return
     */
    Observable<CacheBean> findPasswordByEmail(String email
            , String verifyCode, String newPassword);

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Observable<Object> changePasswordV2(String oldPassword, String newPassword);

}

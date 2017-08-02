package com.zhiyicx.thinksnsplus.data.source.repository.i;

import rx.Observable;

/**
 * @Describe 获取短信验证接口
 * @Author Jungle68
 * @Date 2017/5/25
 * @Contact master.jungle68@gmail.com
 */

public interface IVertifyCodeRepository {
    /**
     * 获取会员验证码 ：使用场景如登陆、找回密码，其他用户行为验证等。
     *
     * @param phone 需要被发送验证码的手机号
     */
    Observable<Object> getMemberVertifyCode(String phone);

    /**
     * 获取会员邮箱验证码 ：使用场景如登陆、找回密码，其他用户行为验证等。
     *
     * @param email 邮箱地址
     */
    Observable<Object> getMemberVerifyCodeByEmail(String email);

    /**
     * 获取非会员验证码 ：用于发送不存在于系统中的用户短信，使用场景如注册等。
     *
     * @param phone 需要被发送验证码的手机号
     */
    Observable<Object> getNonMemberVertifyCode(String phone);

    /**
     * 获取非会员的邮箱验证码，邮箱注册时使用
     *
     * @param email 邮箱地址
     */
    Observable<Object> getNonMemberVerifyCodeByEmail(String email);
}

package com.zhiyicx.thinksnsplus.modules.password;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public interface IPasswordRepository {


    Observable<BaseJson<Boolean>> findPassword(String phone
            , String vertifyCode);
}

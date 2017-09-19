package com.zhiyicx.zhibosdk.model.api;

import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.utils.CommonUtils;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.model.api
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBContants {
    private static ZBUserAuth mUserAuth;

    public static ZBUserAuth getmUserAuth(String seckret) {
        if (CommonUtils.MD5encode(ZBApi.getContantsSeckret()).equals(seckret))
            return mUserAuth;
        else
            return null;

    }

    public static void setmUserAuth(ZBUserAuth mUserAuth) {
        ZBContants.mUserAuth = mUserAuth;
    }


}

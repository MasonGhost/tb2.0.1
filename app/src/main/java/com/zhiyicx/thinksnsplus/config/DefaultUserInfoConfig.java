package com.zhiyicx.thinksnsplus.config;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/19
 * @Contact master.jungle68@gmail.com
 */
public class DefaultUserInfoConfig {

    public static UserInfoBean getDefaultDeletUserInfo(Context context, long user_id) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id(user_id);
        userInfoBean.setName(context.getString(R.string.default_delete_user_name));
        userInfoBean.setHas_deleted(true);
        return userInfoBean;
    }

}

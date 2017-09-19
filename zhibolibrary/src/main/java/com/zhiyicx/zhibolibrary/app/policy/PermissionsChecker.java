package com.zhiyicx.zhibolibrary.app.policy;

/**
 * Created by jungle on 16/9/18.
 * com.zhiyicx.zhibo.app.policy
 * zhibo_android
 * email:335891510@qq.com
 */
public interface PermissionsChecker {
    /**
     * 判断权限集合
     *
     * @param permissions
     * @return
     */
    boolean lacksPermissions(String... permissions);

    /**
     * 判断是否缺少权限
     *
     * @param permission
     * @return
     */
    boolean lacksPermission(String permission);
}

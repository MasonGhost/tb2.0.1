package com.zhiyicx.common.thridmanager;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/20
 * @Contact master.jungle68@qq.com
 */

public interface OnShareCallbackListener {

    void onSuccess();

    void onError(Throwable throwable);

    void onCancel();

}

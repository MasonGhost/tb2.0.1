package com.zhiyicx.common.thridmanager.share;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/20
 * @Contact master.jungle68@qq.com
 */

public interface OnShareCallbackListener {

    void onSuccess(Share share);

    void onError(Share share, Throwable throwable);

    void onCancel(Share share);

}

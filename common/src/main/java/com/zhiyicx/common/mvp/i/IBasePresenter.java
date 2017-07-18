package com.zhiyicx.common.mvp.i;
/**
 * @Describe presenter 公用接口
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public interface IBasePresenter {
    /**
     * 关联 Activity\Fragment 生命周期
     */
    void onStart();

    /**
     * 关联 Activity\Fragment 生命周期
     */
    void onDestroy();

}

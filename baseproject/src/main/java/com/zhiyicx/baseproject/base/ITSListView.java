<<<<<<< HEAD
package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBaseView;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface ITSListView<T,P> extends IBaseView<P> {
    /**
     * 服务器返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onNetResponseSuccess(List<T> data, boolean isLoadMore);
    /**
     * 数据库返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onCacheResponseSuccess(List<T> data, boolean isLoadMore);
    /**
     * 错误信息
     *
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    void onResponseError(Throwable throwable, boolean isLoadMore);


}
=======
package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBaseView;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface ITSListView<T,P> extends IBaseView<P> {
    /**
     * 服务器返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onNetResponseSuccess(List<T> data, boolean isLoadMore);
    /**
     * 数据库返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onCacheResponseSuccess(List<T> data, boolean isLoadMore);
    /**
     * 错误信息
     *
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    void onResponseError(Throwable throwable, boolean isLoadMore);


}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94

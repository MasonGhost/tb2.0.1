package com.zhiyicx.baseproject.base;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface ITSListView<T> {
    /**
     * 服务器返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onResponseSuccess(List<T> data, boolean isLoadMore);

    /**
     * 错误信息
     *
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    void onResponseError(Throwable throwable, boolean isLoadMore);


}

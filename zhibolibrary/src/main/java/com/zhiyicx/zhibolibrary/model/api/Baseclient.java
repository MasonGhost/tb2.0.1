package com.zhiyicx.zhibolibrary.model.api;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public interface Baseclient {
    /**
     * 初始化adapter
     * @param restInterface
     * @param <T>
     * @return
     */
    <T> T initRetrofit(Class<T> restInterface);
}

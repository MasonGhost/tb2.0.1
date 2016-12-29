package com.zhiyicx.thinksnsplus.data.source.local;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public interface CommonCache<T> {
    /**
     * 保存服务器单条数据
     */
    void saveSingleData(T singleData);

    /**
     * 保存服务器多条数据
     */
    void saveMultiData(List<T> multiData);

    /**
     * 判断数据是否失效
     */
    boolean isInvalide();

    /**
     * 根据key(比如userID)，从缓存中获取单条数据
     */
    T getSingleDataFromCache(String key);

    /**
     * 获取表中所有的数据，（比如所有的好友）
     */
    List<T> getMultiDataFromCache();

    /**
     * 清空缓存
     */
    void clearCache();

    /**
     * 根据key，删除缓存中的某条数据
     */
    void deleteSingleCache(String key);

    /**
     * 获取缓存大小,返回MB
     */
    double getCacheSize();

    /**
     * 更新缓存中的某条数据
     */
    void updateSingleData(String key);
}

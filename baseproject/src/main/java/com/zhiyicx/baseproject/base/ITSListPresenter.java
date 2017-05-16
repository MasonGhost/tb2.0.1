package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
public interface ITSListPresenter<T extends BaseListBean> extends IBasePresenter {
    /**
     * 请求列表数据
     *
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore 加载状态
     */
    void requestNetData(Long maxId, boolean isLoadMore);

    /**
     * 请求缓存列表数据
     *
     * @param max_Id     当前获取到数据的最小时间
     * @param isLoadMore 加载状态
     * @return 返回数据按时间排序
     */
    List<T> requestCacheData(Long max_Id, boolean isLoadMore);

    /**
     * 插入或者更新缓存
     */
    boolean insertOrUpdateData(@NotNull List<T> data, boolean isLoadMore);

    /**
     * 判断是否登录
     *
     * @return true  is login
     */
    boolean isLogin();

    /**
     * 处理游客点击事件
     *
     * @return
     */
    boolean handleTouristControl();
}

package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBasePresenter;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
public interface ITSListPresenter extends IBasePresenter {
    /**
     * 请求列表数据
     *
     * @param maxId 当前获取到数据的最大 id
     * @param isLoadMore 加载状态
     */
    void requestData(int maxId,boolean isLoadMore);


}

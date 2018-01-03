package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 通知相关
 * @Author Jungle68
 * @Date 2017/12/25
 * @Contact master.jungle68@gmail.com
 */
public interface INotificationRepository {

    /**
     * 获取通知列表
     *
     * @param size
     * @return
     */
    Observable<List<TSPNotificationBean>> getNotificationList(int size);
}

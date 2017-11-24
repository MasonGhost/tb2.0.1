package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;

/**
 * @Describe 直播相关接口
 * @Author Jungle68
 * @Date 2017/11/19
 * @Contact master.jungle68@gmail.com
 */

public interface ILiveRepository {

    /**
     * 获取直播票据
     *
     * @return
     */
    Observable<String> getLiveTicket();


}

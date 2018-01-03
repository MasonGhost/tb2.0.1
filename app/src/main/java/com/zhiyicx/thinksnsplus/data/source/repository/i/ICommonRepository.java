package com.zhiyicx.thinksnsplus.data.source.repository.i;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UpdateInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 通用接口
 * @Author Jungle68
 * @Date 2017/12/19
 * @Contact master.jungle68@gmail.com
 */

public interface ICommonRepository {
    /**
     * 获取缓存大小
     */
    Observable<String> getDirCacheSize(Context context);

    Observable<List<AllAdverListBean>> getLaunchAdverts();

    Observable<List<RealAdvertListBean>> getRealAdverts(long space_id);

    Observable<List<RealAdvertListBean>> getAllRealAdverts(List<Object> space_id);
    /**
     * 获取缓存大小
     */
    Observable<String> getDirCacheSize();

    /**
     * 清理缓存
     */
    Observable<Boolean> cleanCache();

    /**
     * 检查更新
     *
     * @return update info
     */
    Observable<UpdateInfoBean> checkUpdate();
}

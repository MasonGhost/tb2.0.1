package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

/**
 * @author LiuChao
 * @describe 动态表，包含所有的关联表
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicBeanGreenDaoImpl extends CommonCacheImpl<DynamicBean> {
    public DynamicBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicBean newData) {
        return 0;
    }

    /**
     * 获取热门的动态列表
     *
     * @return
     */
    public List<DynamicBean> getHotDynamicList() {
        return null;
    }

    /**
     * 获取关注的动态列表
     */
    public List<DynamicBean> getFollowedDynamicList() {
        return null;
    }

}

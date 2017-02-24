package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicToolBeanGreenDaoImpl extends CommonCacheImpl<DynamicToolBean> {
    public DynamicToolBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicToolBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicToolBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicToolBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicToolBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicToolBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicToolBean newData) {
        return 0;
    }
}

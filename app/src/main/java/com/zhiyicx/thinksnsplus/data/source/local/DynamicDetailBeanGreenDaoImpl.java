package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicDetailBeanGreenDaoImpl extends CommonCacheImpl<DynamicDetailBean> {
    public DynamicDetailBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicDetailBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicDetailBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicDetailBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicDetailBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicDetailBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicDetailBean newData) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        return dynamicDetailBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicDetailBean> newData) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        dynamicDetailBeanDao.insertOrReplaceInTx(newData);
    }

}

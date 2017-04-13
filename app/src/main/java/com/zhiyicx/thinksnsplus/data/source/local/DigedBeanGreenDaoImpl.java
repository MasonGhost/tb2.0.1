package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 赞列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class DigedBeanGreenDaoImpl extends CommonCacheImpl<DigedBean> {

    @Inject
    public DigedBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DigedBean singleData) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        return digedBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<DigedBean> multiData) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        digedBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DigedBean getSingleDataFromCache(Long primaryKey) {
        DigedBeanDao digedBeanDao = getRDaoSession().getDigedBeanDao();
        return digedBeanDao.load(primaryKey);
    }

    @Override
    public List<DigedBean> getMultiDataFromCache() {
        DigedBeanDao digedBeanDao = getRDaoSession().getDigedBeanDao();
        List<DigedBean> datas = digedBeanDao.queryDeep(" where "
                        +" T."  + DigedBeanDao.Properties.Id.columnName + " < ? "
                        + " order by " + " T."  + DigedBeanDao.Properties.Id.columnName  + " DESC"// 按频道id倒序
                , System.currentTimeMillis() + "");
        return datas;
    }

    @Override
    public void clearTable() {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        digedBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        digedBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DigedBean dta) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        digedBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(DigedBean newData) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        digedBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(DigedBean newData) {
        DigedBeanDao digedBeanDao = getWDaoSession().getDigedBeanDao();
        return digedBeanDao.insertOrReplace(newData);
    }

}

package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/30/9:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleInfoGreenDaoImpl extends CommonCacheImpl<CircleInfo> {

    private CircleInfoDao mCircleInfoRDao, mCircleInfoWDao;

    @Inject
    public CircleInfoGreenDaoImpl(Application context) {
        super(context);
        mCircleInfoRDao = getRDaoSession().getCircleInfoDao();
        mCircleInfoWDao = getWDaoSession().getCircleInfoDao();
    }

    @Override
    public long saveSingleData(CircleInfo singleData) {
        return mCircleInfoWDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<CircleInfo> multiData) {
        mCircleInfoWDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CircleInfo getSingleDataFromCache(Long primaryKey) {
        return mCircleInfoRDao.load(primaryKey);
    }

    @Override
    public List<CircleInfo> getMultiDataFromCache() {
        return mCircleInfoRDao.loadAll();
    }

    @Override
    public void clearTable() {
        mCircleInfoWDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mCircleInfoWDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CircleInfo dta) {
        mCircleInfoWDao.delete(dta);
    }

    @Override
    public void updateSingleData(CircleInfo newData) {
        saveSingleData(newData);
    }

    @Override
    public long insertOrReplace(CircleInfo newData) {
        return 0;
    }

    public List<CircleInfo> getCircleListByCategory(long categoryId) {
        return mCircleInfoRDao.queryBuilder()
                .where(CircleInfoDao.Properties.Category_id.eq(categoryId))
                .orderDesc(CircleInfoDao.Properties.Created_at)
                .build()
                .list();
    }
}

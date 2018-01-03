package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/28/14:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleTypeBeanGreenDaoImpl extends CommonCacheImpl<CircleTypeBean> {

    CircleTypeBeanDao mCircleTypeBeanWDao = getWDaoSession().getCircleTypeBeanDao();
    CircleTypeBeanDao mCircleTypeBeanRDao = getRDaoSession().getCircleTypeBeanDao();

    @Inject
    public CircleTypeBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CircleTypeBean singleData) {
        return mCircleTypeBeanWDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<CircleTypeBean> multiData) {
        mCircleTypeBeanWDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CircleTypeBean getSingleDataFromCache(Long primaryKey) {
        return mCircleTypeBeanRDao.load(primaryKey);
    }

    @Override
    public List<CircleTypeBean> getMultiDataFromCache() {
        return mCircleTypeBeanRDao.loadAll();
    }

    @Override
    public void clearTable() {
        mCircleTypeBeanWDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mCircleTypeBeanWDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CircleTypeBean dta) {
        mCircleTypeBeanWDao.delete(dta);
    }

    @Override
    public void updateSingleData(CircleTypeBean newData) {

    }

    @Override
    public long insertOrReplace(CircleTypeBean newData) {
        return 0;
    }

    public String getCategoryNameById(int id) {
        CircleTypeBean data = mCircleTypeBeanRDao.queryBuilder().where(CircleTypeBeanDao.Properties.Id.eq(id)).unique();
        if (data == null) {
            return "";
        } else {
            return data.getName();
        }
    }
}

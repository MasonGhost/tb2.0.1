package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/24
 * @contact email:648129313@qq.com
 */

public class RankIndexBeanGreenDaoImpl extends CommonCacheImpl<RankIndexBean>{

    private RankIndexBeanDao mRankIndexBeanDao;

    @Inject
    public RankIndexBeanGreenDaoImpl(Application context) {
        super(context);
        mRankIndexBeanDao = getRDaoSession().getRankIndexBeanDao();
    }

    @Override
    public long saveSingleData(RankIndexBean singleData) {
        return mRankIndexBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<RankIndexBean> multiData) {
        mRankIndexBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public RankIndexBean getSingleDataFromCache(Long primaryKey) {
        return mRankIndexBeanDao.load(primaryKey);
    }

    @Override
    public List<RankIndexBean> getMultiDataFromCache() {
        return mRankIndexBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mRankIndexBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mRankIndexBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(RankIndexBean dta) {
        mRankIndexBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(RankIndexBean newData) {
        mRankIndexBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(RankIndexBean newData) {
        return mRankIndexBeanDao.insertOrReplace(newData);
    }

    public List<RankIndexBean> getIndexRankList(String type){
        List<RankIndexBean> list = new ArrayList<>();
        list.addAll(mRankIndexBeanDao.queryBuilder().where(RankIndexBeanDao.Properties.Category.eq(type)).build().list());
        return list;
    }
}

package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/14/16:14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoRecommendBeanGreenDaoImpl extends CommonCacheImpl<InfoRecommendBean> {

    InfoRecommendBeanDao mInfoRecommendBeanDao;

    @Inject
    public InfoRecommendBeanGreenDaoImpl(Application context) {
        super(context);
        mInfoRecommendBeanDao=getWDaoSession().getInfoRecommendBeanDao();
    }

    @Override
    public long saveSingleData(InfoRecommendBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<InfoRecommendBean> multiData) {
        mInfoRecommendBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoRecommendBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<InfoRecommendBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoRecommendBean dta) {

    }

    @Override
    public void updateSingleData(InfoRecommendBean newData) {

    }

    @Override
    public long insertOrReplace(InfoRecommendBean newData) {
        return 0;
    }
}

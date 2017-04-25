package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/14/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListDataBeanGreenDaoImpl extends CommonCacheImpl<InfoListDataBean> {

    InfoListDataBeanDao mInfoListDataBeanDao;

    @Inject
    public InfoListDataBeanGreenDaoImpl(Application context) {
        super(context);
        mInfoListDataBeanDao = getWDaoSession().getInfoListDataBeanDao();
    }

    @Override
    public long saveSingleData(InfoListDataBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<InfoListDataBean> multiData) {
        mInfoListDataBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoListDataBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<InfoListDataBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoListDataBean dta) {
        mInfoListDataBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(InfoListDataBean newData) {
        mInfoListDataBeanDao.insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(InfoListDataBean newData) {
        return 0;
    }

    public boolean isDiged(int news_id) {
        try {
            return mInfoListDataBeanDao.queryBuilder().where(InfoListDataBeanDao.Properties.Id.eq(news_id)).build().list().get(0).getIs_digg_news() == 1;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean isCollected(int news_id) {
        try {
            return mInfoListDataBeanDao.queryBuilder().where(InfoListDataBeanDao.Properties.Id.eq(news_id)).build().list()
                    .get(0).getIs_collection_news() == 1;
        } catch (Exception e) {
            return false;
        }

    }
}

package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
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
        return mInfoListDataBeanDao.insertOrReplace(singleData);
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
        return getRDaoSession().getInfoListDataBeanDao().load(primaryKey);
    }

    @Override
    public List<InfoListDataBean> getMultiDataFromCache() {
        return getRDaoSession().getInfoListDataBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getInfoListDataBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getInfoListDataBeanDao().deleteByKey(primaryKey);
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
            return mInfoListDataBeanDao.queryBuilder().where(InfoListDataBeanDao.Properties.Id.eq(news_id)).build().list().get(0).getHas_like();
        } catch (Exception e) {
            return false;
        }

    }

    public boolean isCollected(int news_id) {
        try {
            return mInfoListDataBeanDao.queryBuilder().where(InfoListDataBeanDao.Properties.Id.eq(news_id)).build().list()
                    .get(0).getHas_collect();
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 根据分类id来获取列表
     *
     * @param cate_id 分类id
     * @return list
     */
    public List<InfoListDataBean> getInfoByType(Long cate_id) {
        try {
            return mInfoListDataBeanDao.queryBuilder().where(InfoListDataBeanDao.Properties.Info_type.eq(cate_id)).build().list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

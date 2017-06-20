package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBeanGreenDaoImpl extends CommonCacheImpl<InfoListBean> {

    private InfoListBeanDao mInfoListBeanDao;
    @Inject
    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    @Inject
    public InfoListBeanGreenDaoImpl(Application context) {
        super(context);
        mInfoListBeanDao = getWDaoSession().getInfoListBeanDao();
    }

    @Override
    public long saveSingleData(InfoListBean singleData) {
        return insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<InfoListBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoListBean getSingleDataFromCache(Long primaryKey) {
        return mInfoListBeanDao.load(primaryKey);
    }

    @Override
    public List<InfoListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoListBean dta) {

    }

    @Override
    public void updateSingleData(InfoListBean newData) {

    }

    @Override
    public long insertOrReplace(InfoListBean newData) {
        if (newData == null) {
            return -1;
        }
        return mInfoListBeanDao.insertOrReplace(newData);
    }

    public InfoListBean getInfoListByInfoType(long info_type) {
        List<InfoListBean> infoListBeen = mInfoListBeanDao.queryBuilder()
                .where(InfoListBeanDao.Properties.Info_type.eq(info_type))
                .list();
        if (infoListBeen.isEmpty()) {
            return null;
        }
        return infoListBeen.get(0);
    }

    @Deprecated
    private void saveCollect(int info_type, int info_id, int is_collection_news) {
        InfoListBean infoListBean = getInfoListByInfoType(info_type);
        for (InfoListDataBean data : infoListBean.getList()) {
            if (data.getId() == info_id) {
                data.setIs_collection_news(is_collection_news);
                mInfoListDataBeanGreenDao.updateSingleData(data);
            }
        }
    }

    public void deleteInfo(InfoListDataBean data){
        mInfoListDataBeanGreenDao.deleteSingleCache(data);
    }

    public void saveCollect(InfoListDataBean data, int is_collection_news) {
        data.setIs_collection_news(is_collection_news);
        mInfoListDataBeanGreenDao.updateSingleData(data);
    }

    public void saveDig(InfoListDataBean data, int is_dig_news) {
        data.setIs_digg_news(is_dig_news);
        mInfoListDataBeanGreenDao.updateSingleData(data);
    }

    public boolean isDiged(int news_id) {
        return mInfoListDataBeanGreenDao.isDiged(news_id);
    }

    public boolean isCollected(int news_id) {
        return mInfoListDataBeanGreenDao.isCollected(news_id);
    }
}

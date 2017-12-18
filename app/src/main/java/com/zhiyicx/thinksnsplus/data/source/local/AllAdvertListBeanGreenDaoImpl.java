package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.config.AdvertConfig;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/01/10:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllAdvertListBeanGreenDaoImpl extends CommonCacheImpl<AllAdverListBean> {

    private AllAdverListBeanDao mAllAdverListBeanDao;

    @Inject
    public AllAdvertListBeanGreenDaoImpl(Application context) {
        super(context);
        mAllAdverListBeanDao = getWDaoSession().getAllAdverListBeanDao();
    }

    @Override
    public long saveSingleData(AllAdverListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<AllAdverListBean> multiData) {
        mAllAdverListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AllAdverListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<AllAdverListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mAllAdverListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(AllAdverListBean dta) {

    }

    @Override
    public void updateSingleData(AllAdverListBean newData) {

    }

    @Override
    public long insertOrReplace(AllAdverListBean newData) {
        return 0;
    }

    public AllAdverListBean getBootAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_BOOT_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getDynamicBannerAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_DYNAMIC_BANNER_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getDynamicListAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_DYNAMIC_LIST_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getDynamicDetailAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_DYNAMIC_DETAILS_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getInfoBannerAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_INFO_BANNER_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getInfoListAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_INFO_LIST_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getInfoDetailAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_INFO_DETAILS_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    public AllAdverListBean getCircleTopAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Space.eq(AdvertConfig.APP_GROUP_TOP_ADVERT)).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

}

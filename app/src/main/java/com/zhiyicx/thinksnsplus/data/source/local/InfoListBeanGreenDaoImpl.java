package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBeanDao;
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
    public InfoListBeanGreenDaoImpl(Context context) {
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

    public InfoListBean getInfoListByInfoType(int info_type) {
        List<InfoListBean> infoListBeen = mInfoListBeanDao.queryBuilder()
                .where(InfoListBeanDao.Properties.Info_type.eq(info_type))
                .list();
        if (infoListBeen.isEmpty()) {
            return null;
        }
        return infoListBeen.get(0);
    }

    public void saveCollect(int info_type, int info_id, int is_collection_news) {
        InfoListBean infoListBean = getInfoListByInfoType(info_type);
        for (InfoListBean.ListBean data : infoListBean.getList()) {
            if (data.getId() == info_id) {
                data.setIs_collection_news(is_collection_news);
            }
        }
        insertOrReplace(infoListBean);
    }
}

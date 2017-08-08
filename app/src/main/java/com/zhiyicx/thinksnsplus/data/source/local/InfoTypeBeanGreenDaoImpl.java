package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoTypeBeanGreenDaoImpl extends CommonCacheImpl<InfoTypeBean> {

    private InfoTypeCatesBeanDao mInfoTypeCatesBeanDao;

    @Inject
    public InfoTypeBeanGreenDaoImpl(Application context) {
        super(context);
        mInfoTypeCatesBeanDao = getWDaoSession().getInfoTypeCatesBeanDao();
    }

    @Override
    public long saveSingleData(InfoTypeBean singleData) {
        clearTable();
        for (InfoTypeCatesBean myCates:singleData.getMy_cates()){
            myCates.setIsMyCate(true);
        }
        singleData.getMy_cates().addAll(singleData.getMore_cates());
        mInfoTypeCatesBeanDao.insertOrReplaceInTx(singleData.getMy_cates());
        return 0;
    }

    @Override
    public void saveMultiData(List<InfoTypeBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoTypeBean getSingleDataFromCache(Long primaryKey) {
        if (getMyCatesList() == null) {
            return null;
        }
        InfoTypeBean infoTypeBean = new InfoTypeBean();
        infoTypeBean.setMy_cates(getMyCatesList());
        infoTypeBean.setMore_cates(getMoreCatesList());
        return infoTypeBean;
    }

    @Override
    public List<InfoTypeBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mInfoTypeCatesBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoTypeBean dta) {

    }

    @Override
    public void updateSingleData(InfoTypeBean newData) {
        clearTable();
        for (InfoTypeCatesBean myCates:newData.getMy_cates()){
            myCates.setIsMyCate(true);
        }
        List<InfoTypeCatesBean> saveDatas=new ArrayList<>();
        saveDatas.addAll(newData.getMy_cates());
        saveDatas.addAll(newData.getMore_cates());
        mInfoTypeCatesBeanDao.insertOrReplaceInTx(saveDatas);
    }

    @Override
    public long insertOrReplace(InfoTypeBean newData) {
        saveSingleData(newData);
        return 0;
    }

    public List<InfoTypeCatesBean> getMyCatesList() {
        return  mInfoTypeCatesBeanDao
                .queryBuilder()
                .where(InfoTypeCatesBeanDao.Properties.IsMyCate.eq(true))
                .build().list();
    }

    public List<InfoTypeCatesBean> getMoreCatesList() {
        return mInfoTypeCatesBeanDao
                .queryBuilder()
                .where(InfoTypeCatesBeanDao.Properties.IsMyCate.notEq(true))
                .build().list();
    }

    public List<InfoTypeCatesBean> getAllCatesList() {
        List<InfoTypeCatesBean> datas=getMyCatesList();
        datas.addAll(getMoreCatesList());
        return datas;
    }

}

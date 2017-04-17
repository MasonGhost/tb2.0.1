package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMyCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMyCatesBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoTypeBeanGreenDaoImpl extends CommonCacheImpl<InfoTypeBean> {

    private InfoTypeMyCatesBeanDao mInfoTypeMyCatesBeanDao;
    private InfoTypeMoreCatesBeanDao mTypeMoreCatesBeanDao;

    @Inject
    public InfoTypeBeanGreenDaoImpl(Context context) {
        super(context);
        mInfoTypeMyCatesBeanDao = getWDaoSession().getInfoTypeMyCatesBeanDao();
        mTypeMoreCatesBeanDao = getWDaoSession().getInfoTypeMoreCatesBeanDao();
    }

    @Override
    public long saveSingleData(InfoTypeBean singleData) {
        mInfoTypeMyCatesBeanDao.insertOrReplaceInTx(singleData.getMy_cates());
        mTypeMoreCatesBeanDao.insertOrReplaceInTx(singleData.getMore_cates());
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
        mInfoTypeMyCatesBeanDao.deleteAll();
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
        mInfoTypeMyCatesBeanDao.insertOrReplaceInTx(newData.getMy_cates());
        mTypeMoreCatesBeanDao.insertOrReplaceInTx(newData.getMore_cates());
    }

    @Override
    public long insertOrReplace(InfoTypeBean newData) {
        saveSingleData(newData);
        return 0;
    }

    public List<InfoTypeMyCatesBean> getMyCatesList() {
        List<InfoTypeMyCatesBean> data = mInfoTypeMyCatesBeanDao.loadAll();
        return data;
    }

    public List<InfoTypeMoreCatesBean> getMoreCatesList() {
        List<InfoTypeMoreCatesBean> data = mTypeMoreCatesBeanDao.loadAll();
        return data;
    }

}

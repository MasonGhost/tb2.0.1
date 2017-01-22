package com.zhiyicx.thinksnsplus.data.beans;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.local.CommonCacheImpl;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/22
 * @contact email:450127106@qq.com
 */

public class EditConfigBeanDaoImpl extends CommonCacheImpl<EditConfigBean> {
    public EditConfigBeanDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveSingleData(EditConfigBean singleData) {
        EditConfigBeanDao editConfigBeanDao = getWDaoSession().getEditConfigBeanDao();
        //editConfigBeanDao.insert(singleData);
        editConfigBeanDao.save(singleData);
    }

    @Override
    public void saveMultiData(List<EditConfigBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public EditConfigBean getSingleDataFromCache(String key) {
        return null;
    }

    @Override
    public List<EditConfigBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(String key) {

    }

    @Override
    public void updateSingleData(EditConfigBean newData) {

    }
}

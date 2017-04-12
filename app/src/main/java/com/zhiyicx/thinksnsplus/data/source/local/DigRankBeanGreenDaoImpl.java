package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 赞列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class DigRankBeanGreenDaoImpl extends CommonCacheImpl<DigRankBean> {

    @Inject
    public DigRankBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DigRankBean singleData) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        return digRankBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<DigRankBean> multiData) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        digRankBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DigRankBean getSingleDataFromCache(Long primaryKey) {
        DigRankBeanDao digRankBeanDao = getRDaoSession().getDigRankBeanDao();
        return digRankBeanDao.load(primaryKey);
    }

    @Override
    public List<DigRankBean> getMultiDataFromCache() {
        DigRankBeanDao digRankBeanDao = getRDaoSession().getDigRankBeanDao();
        List<DigRankBean> datas = digRankBeanDao.loadAll();

        Collections.sort(datas, new Comparator<DigRankBean>() {
            @Override
            public int compare(DigRankBean o1, DigRankBean o2) {
                try {
                    return Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return datas;
    }

    @Override
    public void clearTable() {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        digRankBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        digRankBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DigRankBean dta) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        digRankBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(DigRankBean newData) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        digRankBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(DigRankBean newData) {
        DigRankBeanDao digRankBeanDao = getWDaoSession().getDigRankBeanDao();
        return digRankBeanDao.insertOrReplace(newData);
    }

}

package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe (意见反馈 + 系统公告) 表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationBeanGreenDaoImpl extends CommonCacheImpl<SystemConversationBean> {

    @Inject
    public SystemConversationBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(SystemConversationBean singleData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<SystemConversationBean> multiData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public SystemConversationBean getSingleDataFromCache(Long primaryKey) {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.load(primaryKey);
    }

    @Override
    public List<SystemConversationBean> getMultiDataFromCache() {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        List<SystemConversationBean> datas = systemConversationBeanDao.queryDeep(" where "
                        + " T." + SystemConversationBeanDao.Properties.Id.columnName + " < ? "
                        + " order by " + " T." + SystemConversationBeanDao.Properties.Id.columnName + " DESC"// 按频道id倒序
                , System.currentTimeMillis() + "");
        return datas;
    }

    @Override
    public void clearTable() {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(SystemConversationBean dta) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(SystemConversationBean newData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(SystemConversationBean newData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.insertOrReplace(newData);
    }

}

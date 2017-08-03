package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 标签数据库操作类
 * @Author Jungle68
 * @Date 2017/7/31
 * @Contact master.jungle68@gmail.com
 */
public class UserTagBeanGreenDaoImpl extends CommonCacheImpl<UserTagBean> {

    @Inject
    public UserTagBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(UserTagBean singleData) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        return userTagBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<UserTagBean> multiData) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        userTagBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public UserTagBean getSingleDataFromCache(Long primaryKey) {
        UserTagBeanDao userTagBeanDao = getRDaoSession().getUserTagBeanDao();
        return userTagBeanDao.load(primaryKey);
    }


    @Override
    public List<UserTagBean> getMultiDataFromCache() {
        UserTagBeanDao userTagBeanDao = getRDaoSession().getUserTagBeanDao();
        return userTagBeanDao.loadAll();
    }

    /**
     * 更具分类 id 获取标签
     *
     * @param categoryId 分类 id
     * @return
     */
    public List<UserTagBean> getTagsByCategoryId(long categoryId) {
        UserTagBeanDao userTagBeanDao = getRDaoSession().getUserTagBeanDao();
        return userTagBeanDao.queryBuilder()
                .where(UserTagBeanDao.Properties.Tag_category_id.eq(categoryId))
                .list();
    }

    /**
     * 标记当前用户上否有这个 tag
     *
     * @param tagId
     * @param mineHas
     * @return
     */
    public boolean markMineTag(long tagId, boolean mineHas) {
        UserTagBean userTagBean = getSingleDataFromCache(tagId);
        if (userTagBean == null || userTagBean.isMine_has() == mineHas) {
            return false;
        }

        userTagBean.setMine_has(mineHas);
        if (insertOrReplace(userTagBean) > 0) {
            return true;
        }

        return false;

    }

    @Override
    public void clearTable() {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        userTagBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        userTagBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(UserTagBean dta) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        userTagBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(UserTagBean newData) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        userTagBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(UserTagBean newData) {
        UserTagBeanDao userTagBeanDao = getWDaoSession().getUserTagBeanDao();
        return userTagBeanDao.insertOrReplace(newData);
    }


}

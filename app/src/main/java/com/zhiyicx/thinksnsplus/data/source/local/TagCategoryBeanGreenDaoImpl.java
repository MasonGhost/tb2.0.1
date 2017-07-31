package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 标签一级分类数据库操作类
 * @Author Jungle68
 * @Date 2017/7/31
 * @Contact master.jungle68@gmail.com
 */
public class TagCategoryBeanGreenDaoImpl extends CommonCacheImpl<TagCategoryBean> {

    @Inject
    public TagCategoryBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(TagCategoryBean singleData) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        return tagCategoryBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<TagCategoryBean> multiData) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        tagCategoryBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public TagCategoryBean getSingleDataFromCache(Long primaryKey) {
        TagCategoryBeanDao tagCategoryBeanDao = getRDaoSession().getTagCategoryBeanDao();
        return tagCategoryBeanDao.load(primaryKey);
    }


    @Override
    public List<TagCategoryBean> getMultiDataFromCache() {
        TagCategoryBeanDao tagCategoryBeanDao = getRDaoSession().getTagCategoryBeanDao();
        return tagCategoryBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        tagCategoryBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        tagCategoryBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(TagCategoryBean dta) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        tagCategoryBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(TagCategoryBean newData) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        tagCategoryBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(TagCategoryBean newData) {
        TagCategoryBeanDao tagCategoryBeanDao = getWDaoSession().getTagCategoryBeanDao();
        return tagCategoryBeanDao.insertOrReplace(newData);
    }


}

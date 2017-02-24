package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicCommentBeanGreenDaoImpl extends CommonCacheImpl<DynamicCommentBean> {
    public DynamicCommentBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicCommentBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicCommentBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicCommentBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicCommentBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicCommentBean newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        return dynamicCommentBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicCommentBean> newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.insertOrReplaceInTx(newData);
    }

}

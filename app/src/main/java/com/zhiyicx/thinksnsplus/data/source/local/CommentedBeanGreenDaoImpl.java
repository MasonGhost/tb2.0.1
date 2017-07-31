package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 评论的列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
public class CommentedBeanGreenDaoImpl extends CommonCacheImpl<CommentedBean> {

    @Inject
    public CommentedBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CommentedBean singleData) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        return commentedBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<CommentedBean> multiData) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        commentedBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CommentedBean getSingleDataFromCache(Long primaryKey) {
        CommentedBeanDao commentedBeanDao = getRDaoSession().getCommentedBeanDao();
        return commentedBeanDao.load(primaryKey);
    }

    @Override
    public List<CommentedBean> getMultiDataFromCache() {
        CommentedBeanDao commentedBeanDao = getRDaoSession().getCommentedBeanDao();
        return commentedBeanDao.queryDeep(" where "
                        + " T." + CommentedBeanDao.Properties.Id.columnName + " < ? "
                        + " order by " + " T." + CommentedBeanDao.Properties.Id.columnName + " DESC"// 按频道id倒序
                , System.currentTimeMillis() + "");
    }

    public List<CommentedBean> getAblumCommentsCacheDataByType(String channel,long id) {
        CommentedBeanDao commentedBeanDao = getRDaoSession().getCommentedBeanDao();
        return commentedBeanDao.queryDeep(" where "
                        + " T." + CommentedBeanDao.Properties.Channel.eq(channel) + " AND "
                        + " T." + CommentedBeanDao.Properties.Target_id.eq(id)
                        + " order by " + " T." + CommentedBeanDao.Properties.Id.columnName + " DESC"// 按频道id倒序
                , System.currentTimeMillis() + "");
    }

    @Override
    public void clearTable() {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        commentedBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        commentedBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CommentedBean dta) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        commentedBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(CommentedBean newData) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        commentedBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(CommentedBean newData) {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        return commentedBeanDao.insertOrReplace(newData);
    }

    public CommentedBean getLastData() {
        CommentedBeanDao commentedBeanDao = getWDaoSession().getCommentedBeanDao();
        List<CommentedBean> datas = commentedBeanDao.queryBuilder()
                .orderDesc(CommentedBeanDao.Properties.Id)
                .limit(1)
                .list();
        if (datas.isEmpty()) {
            return null;
        }
        return datas.get(0);
    }
}

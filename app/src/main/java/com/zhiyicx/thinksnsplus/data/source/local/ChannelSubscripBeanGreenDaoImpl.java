package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/10
 * @contact email:450127106@qq.com
 */

public class ChannelSubscripBeanGreenDaoImpl extends CommonCacheImpl<ChannelSubscripBean> {
    @Inject
    public ChannelSubscripBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(ChannelSubscripBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<ChannelSubscripBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public ChannelSubscripBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<ChannelSubscripBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        ChannelSubscripBeanDao channelSubscripBeanDao = getWDaoSession().getChannelSubscripBeanDao();
        channelSubscripBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(ChannelSubscripBean dta) {

    }

    @Override
    public void updateSingleData(ChannelSubscripBean newData) {

    }

    @Override
    public long insertOrReplace(ChannelSubscripBean newData) {
        if (newData == null) {
            return -1;
        }
        ChannelSubscripBeanDao channelSubscripBeanDao = getWDaoSession().getChannelSubscripBeanDao();
        return channelSubscripBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<ChannelSubscripBean> newData) {
        if (newData == null) {
            return;
        }
        ChannelSubscripBeanDao channelSubscripBeanDao = getWDaoSession().getChannelSubscripBeanDao();
        channelSubscripBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取所有的频道列表，当前没有传入 maxid，因为界面会直接显示所有的频道
     * ChannelSubscriped 订阅状态 0 false 1 true
     *
     * @param userId 表中存的是该 userid 请求得到的频道列表，主要是为了突出重复
     */
    public List<ChannelSubscripBean> getAllChannelList(long userId) {
        ChannelSubscripBeanDao channelSubscripBeanDao = getRDaoSession().getChannelSubscripBeanDao();
        return channelSubscripBeanDao.queryDeep(" where "
                        + ChannelSubscripBeanDao.Properties.UserId.columnName + " = ? "
                        + " order by " + "T.\"" + ChannelSubscripBeanDao.Properties.Id.columnName + "\"" + " DESC"// 按频道id倒序
                , userId + "");
    }

    /**
     * 获取某人订阅的频道列表，当前没有传入 maxid，因为界面会直接显示所有的频道
     *
     * @param userId 当前 userid 的用户所关注的频道列表
     */
    public List<ChannelSubscripBean> getSomeOneSubscribChannelList(long userId) {
        ChannelSubscripBeanDao channelSubscripBeanDao = getRDaoSession().getChannelSubscripBeanDao();
        return channelSubscripBeanDao.queryDeep(" where "
                        + ChannelSubscripBeanDao.Properties.UserId.columnName + " = ? and "
                        + ChannelSubscripBeanDao.Properties.ChannelSubscriped.columnName + " = ? " // 订阅状态
                        + " order by " + "T.\"" + ChannelSubscripBeanDao.Properties.Id.columnName + "\"" + " DESC"// 按频道id倒序
                , userId + ""
                , "1");
    }

    /**
     * 清空所有和userId相同的频道
     *
     * @param userId
     */
    public void clearTableByUserId(long userId) {
        ChannelSubscripBeanDao channelSubscripBeanDao = getWDaoSession().getChannelSubscripBeanDao();
        QueryBuilder queryBuilder = channelSubscripBeanDao.queryBuilder();
        queryBuilder.where(ChannelSubscripBeanDao.Properties.UserId.eq(userId));
        channelSubscripBeanDao.deleteInTx(queryBuilder.list());
    }

}

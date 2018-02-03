package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/02/03/14:54
 * @Email Jliuer@aliyun.com
 * @Description 群信息管理
 */
public class ChatGroupBeanGreenDaoImpl extends CommonCacheImpl<ChatGroupBean> {

    ChatGroupBeanDao readDao, writeDao;

    @Inject
    public ChatGroupBeanGreenDaoImpl(Application context) {
        super(context);
        readDao = getRDaoSession().getChatGroupBeanDao();
        writeDao = getWDaoSession().getChatGroupBeanDao();
    }

    @Override
    public long saveSingleData(ChatGroupBean singleData) {
        return writeDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<ChatGroupBean> multiData) {
        writeDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public ChatGroupBean getSingleDataFromCache(Long primaryKey) {
        return readDao.load(primaryKey);
    }

    @Override
    public List<ChatGroupBean> getMultiDataFromCache() {
        return readDao.loadAll();
    }

    @Override
    public void clearTable() {
        writeDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        writeDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(ChatGroupBean dta) {
        writeDao.delete(dta);
    }

    @Override
    public void updateSingleData(ChatGroupBean newData) {
        saveSingleData(newData);
    }

    @Override
    public long insertOrReplace(ChatGroupBean newData) {
        return saveSingleData(newData);
    }

    public ChatGroupBean getChatGroupBeanById(String id) {
        return readDao.queryBuilder().where(ChatGroupBeanDao.Properties.Id.eq(id)).build().unique();
    }

    public List<ChatGroupBean> getChatGroupBeanByIds(List<String> ids) {
        List<ChatGroupBean> result = new ArrayList<>();
        if (ids.size() <= 0) {
            return result;
        }
        for (String id : ids) {
            ChatGroupBean chatGroupBean = getChatGroupBeanById(id);
            if (chatGroupBean != null) {
                result.add(chatGroupBean);
            }
        }
        return result;
    }
}

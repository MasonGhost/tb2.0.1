package com.zhiyicx.thinksnsplus.data.source.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhiyicx.baseproject.cache.IDataBaseOperate;
import com.zhiyicx.baseproject.config.DBConfig;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DaoMaster;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public abstract class CommonCacheImpl<T> implements IDataBaseOperate<T> {
    private static final UpDBHelper sUpDBHelper = new UpDBHelper(AppApplication.getContext(), DBConfig.DB_NAME);

    public CommonCacheImpl(Context context) {
    }

    /**
     * 获取可读数据库
     */
    protected SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = sUpDBHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    protected SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = sUpDBHelper.getWritableDatabase();
        return db;
    }

    /**
     * 获取可写数据库的DaoMaster
     */
    protected DaoMaster getWDaoMaster() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        return daoMaster;
    }

    /**
     * 获取可写数据库的DaoSession
     */
    protected DaoSession getWDaoSession() {
        DaoSession daoSession = getWDaoMaster().newSession();
        return daoSession;
    }

    /**
     * 获取可写数据库的DaoMaster
     */
    protected DaoMaster getRDaoMaster() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        return daoMaster;
    }

    /**
     * 获取可写数据库的DaoSession
     */
    protected DaoSession getRDaoSession() {
        DaoSession daoSession = getRDaoMaster().newSession();
        return daoSession;
    }
}

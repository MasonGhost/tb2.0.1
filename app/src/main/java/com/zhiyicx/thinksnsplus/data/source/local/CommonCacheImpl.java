package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhiyicx.baseproject.cache.IDataBaseOperate;
import com.zhiyicx.baseproject.config.DBConfig;
import com.zhiyicx.thinksnsplus.data.beans.DaoMaster;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public abstract class CommonCacheImpl<T> implements IDataBaseOperate<T> {
    protected DaoMaster.DevOpenHelper mDevOpenHelper;
    private Context mContext;

    public CommonCacheImpl(Context context) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DBConfig.DB_NAME);
        mContext = context;
    }

    /**
     * 获取可读数据库
     */
    protected SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = mDevOpenHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    protected SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
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

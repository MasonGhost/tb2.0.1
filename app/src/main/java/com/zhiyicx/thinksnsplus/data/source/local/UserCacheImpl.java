package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhiyicx.baseproject.config.DBConfig;
import com.zhiyicx.thinksnsplus.data.beans.DaoMaster;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;
import com.zhiyicx.thinksnsplus.data.beans.User;
import com.zhiyicx.thinksnsplus.data.beans.UserDao;

import java.util.List;

/**
 * @author LiuChao
 * @describe 数据库保存服务器数据，实现本地缓存
 * @date 2016/12/29
 * @contact email:450127106@qq.com
 */

public class UserCacheImpl implements CommonCache<User> {

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private Context mContext;

    public UserCacheImpl(Context context) {
        mContext = context;
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DBConfig.DB_NAME);
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = mDevOpenHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        return db;
    }
    DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    UserDao userDao = daoSession.getUserDao();


    @Override
    public void saveSingleData(User singleData) {

    }

    @Override
    public void saveMultiData(List<User> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public User getSingleDataFromCache(String key) {
        return null;
    }

    @Override
    public List<User> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public void deleteSingleCache(String key) {

    }

    @Override
    public double getCacheSize() {
        return 0;
    }

    @Override
    public void updateSingleData(String key) {

    }
}

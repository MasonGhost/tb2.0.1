package com.zhiyicx.imsdk.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jungle on 16/8/12.
 * com.zhiyicx.imsdk.db.base
 * zhibo_android
 * email:335891510@qq.com
 */
public abstract class BaseDao {
    private final String TAG = getClass().getSimpleName();

    protected static final int VERSION = 2;
    protected static final String DB_NAME = "zycxIM.db";

    public static final long TIME_DEFAULT_ADD = 1451577600000L; //  消息的MID，`(mid >> 23) + 1451577600000` 为毫秒时间戳
    protected final int DEFAULT_PAGEE = 1;
    protected final int DEFAULT_PAGESIZE = 20;

    protected Context context;
    protected ZBSqlHelper mHelper;

    public abstract void close();

    public void delDataBase() {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.execSQL(ZBSqlHelper.SQL_DELETE_MESSAGE);
            database.execSQL(ZBSqlHelper.SQL_DELETE_CONVERSATION);
            database.execSQL(ZBSqlHelper.SQL_DELETE_MASK);
            database.execSQL(ZBSqlHelper.SQL_CREATE_MESSAGE);
            database.execSQL(ZBSqlHelper.SQL_CREATE_CONVERSATION);
            database.execSQL(ZBSqlHelper.SQL_CREATE_MASK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}

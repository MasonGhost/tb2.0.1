package com.zhiyicx.imsdk.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MaskDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;

/**
 * INTEGER – 整數，對應Java 的byte、short、int 和long。
 * REAL – 小數，對應Java 的float 和double。
 * TEXT – 字串，對應Java 的String。
 * CREATE TABLE ex2(
 * a VARCHAR(10),
 * b NVARCHAR(15),
 * c TEXT,
 * d INTEGER,
 * e FLOAT,
 * f BOOLEAN,
 * g CLOB,
 * h BLOB,
 * i TIMESTAMP,
 * j NUMERIC(10,5),
 * k VARYING CHARACTER (24),
 * l NATIONAL VARYING CHARACTER(16)
 * );
 * Created by jungle on 16/8/12.
 * com.zhiyicx.imsdk.db.base
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBSqlHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = "  TEXT";
    private static final String INTEGER_TYPE = "  INTEGER";
    private static final String COMMA_SEP = ",";
    /**
     * message数据表
     */
    public static final String SQL_CREATE_MESSAGE =
            "CREATE TABLE " + MessageDao.TABLE_NAME + " (" +
                    MessageDao.COLUMN_NAME_AUTO_INCREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    MessageDao.COLUMN_NAME_MESSAGE_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_MID + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_UID + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_CID + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_TO + TEXT_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_TYPE + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_TXT + TEXT_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_EXT + TEXT_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_RT + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_ERR + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_GAG + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_CREATE_TIME + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_IS_READ + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_SEND_STATUS + INTEGER_TYPE + COMMA_SEP +
                    MessageDao.COLUMN_NAME_MESSAGE_IS_DEL + INTEGER_TYPE +
                    " )";


    public static final String SQL_DELETE_MESSAGE =
            "DROP TABLE IF EXISTS " + MessageDao.TABLE_NAME;


    /**
     * conversation数据表
     */
    public static final String SQL_CREATE_CONVERSATION =
            "CREATE TABLE " + ConversationDao.TABLE_NAME + " (" +
                    ConversationDao.COLUMN_NAME_CONVERSATION_CID + INTEGER_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_TYPE + INTEGER_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_NAME + TEXT_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_DISABLE + INTEGER_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_PAIR + TEXT_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_PWD + TEXT_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_USIDS + TEXT_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_LAST_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME + INTEGER_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_IS_DEL + INTEGER_TYPE + COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_IM_UID + INTEGER_TYPE  +COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_USER_ID + INTEGER_TYPE  +COMMA_SEP +
                    ConversationDao.COLUMN_NAME_CONVERSATION_MC + INTEGER_TYPE  +
                    " )";

    public static final String SQL_DELETE_CONVERSATION =
            "DROP TABLE IF EXISTS " + ConversationDao.TABLE_NAME;

    /**
     * mak数据表 关系表
     */
    public static final String SQL_CREATE_MASK =
            "CREATE TABLE " + MaskDao.TABLE_NAME + " (" +
                    MaskDao.COLUMN_NAME_MASK_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
                    MaskDao.COLUMN_NAME_MASK_CID + INTEGER_TYPE + COMMA_SEP +
                    MaskDao.COLUMN_NAME_MASK_FROM_IM_UID + INTEGER_TYPE + COMMA_SEP +
                    MaskDao.COLUMN_NAME_MASK_TO_IM_UID + INTEGER_TYPE +
                    " )";

    public static final String SQL_DELETE_MASK =
            "DROP TABLE IF EXISTS " + MaskDao.TABLE_NAME;


    public ZBSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE);
        db.execSQL(SQL_CREATE_CONVERSATION);
        db.execSQL(SQL_CREATE_MASK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MESSAGE);
        db.execSQL(SQL_DELETE_CONVERSATION);
        db.execSQL(SQL_DELETE_MASK);
        onCreate(db);
    }
}

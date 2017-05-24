package com.zhiyicx.imsdk.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.imsdk.db.base.BaseDao;
import com.zhiyicx.imsdk.db.base.ZBSqlHelper;
import com.zhiyicx.imsdk.db.dao.soupport.MessageDaoSoupport;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageExt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungle on 16/8/12.
 * com.zhiyicx.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */
public class MessageDao extends BaseDao implements MessageDaoSoupport {
    public static final String TABLE_NAME = "message";
    public static final String COLUMN_NAME_AUTO_INCREMENT_ID = "message_id";//自增长id
    public static final String COLUMN_NAME_MESSAGE_ID = "id";
    public static final String COLUMN_NAME_MESSAGE_MID = "mid";
    public static final String COLUMN_NAME_MESSAGE_UID = "uid";
    public static final String COLUMN_NAME_MESSAGE_CID = "cid";
    public static final String COLUMN_NAME_MESSAGE_TO = "to_uids";
    public static final String COLUMN_NAME_MESSAGE_TYPE = "type";
    public static final String COLUMN_NAME_MESSAGE_TXT = "txt";
    public static final String COLUMN_NAME_MESSAGE_EXT = "ext";
    public static final String COLUMN_NAME_MESSAGE_RT = "rt";
    public static final String COLUMN_NAME_MESSAGE_ERR = "err";
    public static final String COLUMN_NAME_MESSAGE_GAG = "gag";
    public static final String COLUMN_NAME_MESSAGE_CREATE_TIME = "create_time";
    public static final String COLUMN_NAME_MESSAGE_IS_DEL = "is_del";//'是否被删除 1:是 0:否',
    public static final String COLUMN_NAME_MESSAGE_IS_READ = "is_read";//'消息阅读状态 1:是 0:否',
    public static final String COLUMN_NAME_MESSAGE_SEND_STATUS = "send_status";//发送状态 0,发送中，1发成功，2发送失败,

    private volatile static MessageDao instance;

    private MessageDao(Context context) {
        this.context = context;
        mHelper = new ZBSqlHelper(context, DB_NAME, null, VERSION);
    }

    public static MessageDao getInstance(Context context) {

        if (instance == null) {
            synchronized (MessageDao.class) {
                if (instance == null) {
                    instance = new MessageDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 增加新的消息
     *
     * @param message
     * @return
     */
    @Override
    public long insertOrUpdateMessage(Message message) {

        if (message == null)
            throw new IllegalArgumentException("message can not be null");
        long rows = 0;
        if ((message.getId() != 0 && hasMessageById(message.getId())) || (message.getMid() != 0) && hasMessage(message.mid)) {// 插入
            return updateMessage(message);
        } else {//更新
            rows = insertMessage(message);
        }
        return rows;
    }

    @Override
    public long insertMessage(Message message) {
        if (message == null) throw new IllegalArgumentException("message can't be null");
        long resut = -1;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues map = getContentValues(message);
            resut = database.insert(
                    TABLE_NAME, null, map);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            database.endTransaction();
        }

        return resut;
    }

    /**
     * 更新的消息
     *
     * @param message
     * @return
     */
    public long updateMessage(Message message) {
        if (message == null) throw new IllegalArgumentException("message can't be null");
        long resut = -1;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues map = getContentValues(message);
            resut = database.update(TABLE_NAME,
                    map,
                    COLUMN_NAME_MESSAGE_ID + " = ?",
                    new String[]{message.getId() + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            database.endTransaction();
        }

        return resut;
    }

    private ContentValues getContentValues(Message message) {
        ContentValues map = new ContentValues();
        map.put(COLUMN_NAME_MESSAGE_ID, message.id);
        map.put(COLUMN_NAME_MESSAGE_MID, message.mid);
        map.put(COLUMN_NAME_MESSAGE_UID, message.uid);
        map.put(COLUMN_NAME_MESSAGE_CID, message.cid);
        map.put(COLUMN_NAME_MESSAGE_TXT, message.txt);
        map.put(COLUMN_NAME_MESSAGE_TYPE, message.type);
        map.put(COLUMN_NAME_MESSAGE_RT, isRt(message.rt));
        map.put(COLUMN_NAME_MESSAGE_ERR, message.err);
        map.put(COLUMN_NAME_MESSAGE_GAG, message.expire);
        map.put(COLUMN_NAME_MESSAGE_IS_READ, isRead(message.is_read));
        map.put(COLUMN_NAME_MESSAGE_SEND_STATUS, message.send_status);
        map.put(COLUMN_NAME_MESSAGE_IS_DEL, isDel(message.is_del));
        if (message.mid != 0) {//  消息的MID，`(mid >> 23) + 1451577600000` 为毫秒时间戳
            message.create_time = (message.mid >> 23) + TIME_DEFAULT_ADD;
        }
        map.put(COLUMN_NAME_MESSAGE_CREATE_TIME, message.create_time);
        String uid = null;
        if (message.to != null)
            uid = new Gson().toJson(message.to);
        map.put(COLUMN_NAME_MESSAGE_TO, uid);
        String ext = null;
        if (message.ext != null)
            ext = new Gson().toJson(message.ext);
        map.put(COLUMN_NAME_MESSAGE_EXT, ext);
        return map;
    }

    /**
     * 通过 id 查找消息是否存在
     *
     * @param id
     * @return
     */
    @Override
    public boolean hasMessageById(long id) {
        Cursor cursor = null;
        boolean result = false;
        try {
            SQLiteDatabase database = mHelper.getReadableDatabase();
            String sql = "select * from " + TABLE_NAME + " where "
                    + COLUMN_NAME_MESSAGE_ID + " = ?";
            cursor = database.rawQuery(sql,
                    new String[]{String.valueOf(id)});
            result = cursor.moveToFirst();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 通过mid查找消息是否存在
     *
     * @param mid
     * @return
     */
    @Override
    public boolean hasMessage(long mid) {
        Cursor cursor = null;
        boolean result = false;
        try {
            SQLiteDatabase database = mHelper.getReadableDatabase();
            String sql = "select * from " + TABLE_NAME + " where "
                    + COLUMN_NAME_MESSAGE_MID + " = ?";
            cursor = database.rawQuery(sql,
                    new String[]{String.valueOf(mid)});
            result = cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 通过会话id,获取会话的消息
     *
     * @param cid
     * @return
     */
    @Override
    public List<Message> getMessageListByCid(int cid, int page) {
        if (page < DEFAULT_PAGEE)
            page = DEFAULT_PAGEE;
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_NAME_MESSAGE_CID + " = ?", new String[]{cid + ""}, null, null,
                COLUMN_NAME_MESSAGE_CREATE_TIME + "  DESC", (page - DEFAULT_PAGEE) * DEFAULT_PAGESIZE + "," + DEFAULT_PAGESIZE);
        List<Message> messages = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Message message = setMessageValue(cursor);

                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return messages;
    }

    /**
     * 通过会话id,和时间获取会话的消息,
     *
     * @param crateTime
     * @return
     */
    @Override
    public List<Message> getMessageListByCidAndCreateTime(int cid, long crateTime) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_NAME_MESSAGE_CID + " = ? and " + COLUMN_NAME_MESSAGE_CREATE_TIME + " < ? ", new String[]{String.valueOf(cid), String.valueOf(crateTime)}, null, null,
                COLUMN_NAME_MESSAGE_CREATE_TIME + "  DESC", 0 + "," + DEFAULT_PAGESIZE);// 时间降序
        List<Message> messages = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Message message = setMessageValue(cursor);
                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return messages;
    }

    /**
     * Messge 赋值
     *
     * @param cursor
     * @return
     */
    private Message setMessageValue(Cursor cursor) {
        Message message = new Message();
        message.setUid(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_UID)));
        message.setCid(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_CID)));
        message.setId(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_ID)));
        message.setType(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_TYPE)));
        message.setType(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_TYPE)));
        message.setTxt(cursor.getString(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_TXT)));
        message.setRt(isRt(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_RT))));
        message.setErr(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_ERR)));
        message.setExpire(cursor.getLong(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_GAG)));
        message.setMid(cursor.getLong(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_MID)));
        message.setCreate_time(cursor.getLong(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_CREATE_TIME)));
        message.setIs_read(isRead(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_IS_READ))));
        message.setSend_status(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_SEND_STATUS)));
        message.setIs_del(isDel(cursor.getInt(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_IS_DEL))));
        String uid = cursor.getString(cursor
                .getColumnIndex(COLUMN_NAME_MESSAGE_TO));
        try {
            if (!TextUtils.isEmpty(uid))
                message.setTo((List<Integer>) new Gson().fromJson(uid, new TypeToken<List<Integer>>() {
                }.getType()));
            String ext = cursor.getString(cursor
                    .getColumnIndex(COLUMN_NAME_MESSAGE_EXT));
            if (!TextUtils.isEmpty(ext))
                message.setExt(new Gson().fromJson(ext, MessageExt.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 获取最新的一条消息
     *
     * @param cid
     * @return
     */
    @Override
    public Message getLastMessageByCid(int cid) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_NAME_MESSAGE_CID + " = ?", new String[]{cid + ""}, null, null,
                COLUMN_NAME_MESSAGE_CREATE_TIME + "  DESC", "0,1");// 时间降序
        List<Message> messages = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Message message = setMessageValue(cursor);
                messages.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        if (messages.size() > 0)
            return messages.get(0);
        else
            return null;
    }

    @Override
    public int getUnReadMessageCount(int cid) {
        int counts;
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_NAME_MESSAGE_CID + " = ? And " + COLUMN_NAME_MESSAGE_IS_READ + " = ?", new String[]{String.valueOf(cid), String.valueOf(isRead(false))}, null, null, null);
        counts = cursor.getCount();
        cursor.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return counts;
    }

    /**
     * 把消息标记为已读
     *
     * @return
     */
    @Override
    public boolean readMessage(long mid) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            // update message set COLUMN_NAME_MESSAGE_IS_READ = true where mid = mid
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_MESSAGE_IS_READ, isRead(true));
            rows = database.update(TABLE_NAME,
                    cv,
                    "mid = ?",
                    new String[]{mid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0)
            return true;
        else
            return false;
    }

    /**
     * 修改消息状态
     *
     * @param mid        消息 mid
     * @param sendStatus 发送状态 0,发送中，1发成功，2发送失败,
     * @return
     */
    @Override
    public boolean changeMessageSendStausByMid(long mid, int sendStatus) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_MESSAGE_SEND_STATUS, sendStatus);
            rows = database.update(TABLE_NAME,
                    cv,
                    "mid = ?",
                    new String[]{mid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0)
            return true;
        else
            return false;
    }

    /**
     * 测试耗时使用
     *
     * @return
     */
    public int upDate(long cid) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            // update message set COLUMN_NAME_MESSAGE_IS_READ = true where mid = mid
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_MESSAGE_IS_READ, isRead(false));
            rows = database.update(TABLE_NAME,
                    cv,
                    "cid = ?",
                    new String[]{cid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return rows;
    }

    /**
     * 查询数据条数
     *
     * @return
     */
    public long getCounts() {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        String sql = "select count(*) from " + TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 标记该消息已删除
     *
     * @param mid
     * @return
     */
    @Override
    public boolean delMessage(long mid) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_MESSAGE_IS_DEL, isDel(true));
            rows = database.update(TABLE_NAME,
                    cv,
                    "mid = ?",
                    new String[]{mid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0)
            return true;
        else
            return false;
    }

    /**
     * 标记该消息已删除
     *
     * @param cid conversation id
     * @return
     */
    @Override
    public boolean delMessageByCid(int cid) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME_MESSAGE_IS_DEL, isDel(true));
            rows = database.update(TABLE_NAME,
                    cv,
                    "cid = ?",
                    new String[]{cid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0)
            return true;
        else
            return false;
    }

    /**
     * 标记该消息已删除
     *
     * @param cid conversation id
     * @return
     */
    @Override
    public boolean delEverMessageByCid(int cid) {
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            rows = database.delete(TABLE_NAME,
                    "cid = ?",
                    new String[]{cid + ""});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0)
            return true;
        else
            return false;
    }


    private int isRead(boolean isRead) {
        if (isRead) return 1;
        return 0;
    }

    private boolean isRead(int isRead) {
        if (isRead == 1) return true;
        return false;
    }

    private int isDel(boolean isDel) {
        if (isDel) return 1;
        return 0;
    }

    private boolean isDel(int isDel) {
        if (isDel == 1) return true;
        return false;
    }

    private int isRt(boolean isRt) {
        if (isRt) return 1;
        return 0;
    }

    private boolean isRt(int isRt) {
        if (isRt == 1) return true;
        return false;
    }

    @Override
    public void close() {
        mHelper.close();
    }


}

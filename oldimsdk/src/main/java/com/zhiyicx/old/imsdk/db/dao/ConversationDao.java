package com.zhiyicx.old.imsdk.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zhiyicx.old.imsdk.db.base.BaseDao;
import com.zhiyicx.old.imsdk.db.base.ZBSqlHelper;
import com.zhiyicx.old.imsdk.db.dao.soupport.ConversationDaoSoupport;
import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungle on 16/8/12.
 * com.zhiyicx.old.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */
public class ConversationDao extends BaseDao implements ConversationDaoSoupport {
    private static final String TAG = "ConversationDao";
    public static final String TABLE_NAME = "conversation";
    public static final String COLUMN_NAME_CONVERSATION_CID = "cid";
    public static final String COLUMN_NAME_CONVERSATION_TYPE = "type";
    public static final String COLUMN_NAME_CONVERSATION_NAME = "name";
    public static final String COLUMN_NAME_CONVERSATION_DISABLE = "disable";
    public static final String COLUMN_NAME_CONVERSATION_PAIR = "pair";
    public static final String COLUMN_NAME_CONVERSATION_PWD = "pwd";
    public static final String COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME = "last_message_time";
    public static final String COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TEXT = "last_message_text";
    public static final String COLUMN_NAME_CONVERSATION_USIDS = "usids";


    public static final String COLUMN_NAME_CONVERSATION_IS_DEL = "is_del";//'是否被删除 1:是 0:否',
    public static final String COLUMN_NAME_CONVERSATION_IM_UID = "im_uid";//'消息阅读状态 1:是 0:否',
    public static final String COLUMN_NAME_CONVERSATION_MC = "mc";//'当前群聊中的人数,

    private volatile static ConversationDao instance;

    private ConversationDao(Context context) {
        this.context = context.getApplicationContext();
        mHelper = new ZBSqlHelper(context, DB_NAME, null, VERSION);
    }

    public static ConversationDao getInstance(Context context) {

        if (instance == null) {
            synchronized (ConversationDao.class) {
                if (instance == null) {
                    instance = new ConversationDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 增加新的对话
     *
     * @param conversation
     * @return
     */
    @Override
    public long insertConversation(Conversation conversation) {
        if (conversation == null) throw new IllegalArgumentException("conversation can't be null");
        if (hasConversation(conversation.getCid())) {
            updateConversation(conversation);
            return 1;
        }

        long resut = -1;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues map = new ContentValues();
            map.put(COLUMN_NAME_CONVERSATION_CID, conversation.getCid());
            map.put(COLUMN_NAME_CONVERSATION_TYPE, conversation.getType());
            map.put(COLUMN_NAME_CONVERSATION_NAME, conversation.getName());
            map.put(COLUMN_NAME_CONVERSATION_DISABLE, conversation.getDisa());
            map.put(COLUMN_NAME_CONVERSATION_PAIR, conversation.getPair());
            map.put(COLUMN_NAME_CONVERSATION_PWD, conversation.getPwd());
            map.put(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME, conversation.getLast_message_time());
            map.put(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TEXT, conversation.getLast_message_text());
            map.put(COLUMN_NAME_CONVERSATION_USIDS, conversation.getUsids());
            map.put(COLUMN_NAME_CONVERSATION_IS_DEL, isDel(conversation.is_del()));
            map.put(COLUMN_NAME_CONVERSATION_IM_UID, conversation.getIm_uid());
            map.put(COLUMN_NAME_CONVERSATION_MC, conversation.getMc());
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
     * 通过cid 获取对话信息
     *
     * @return
     */
    @Override
    public Conversation getConversationByCid(int cid) {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_NAME_CONVERSATION_CID + " = ?", new String[]{String.valueOf(cid)}, null, null,
                null);
        List<Conversation> conversations = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Conversation conversation = new Conversation();
                conversation.setCid(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_CID)));
                conversation.setType(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_TYPE)));
                conversation.setName(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_NAME)));
                conversation.setPair(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_PAIR)));
                conversation.setPwd(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_PWD)));
                conversation.setLast_message_time(cursor.getLong(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME)));
                conversation.setLast_message_text(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TEXT)));
                conversation.setUsids(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_USIDS)));
                conversation.setIm_uid(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_IM_UID)));
                conversation.setIs_del(isDel(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_IS_DEL))));
                conversation.setDisa(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_DISABLE)));
                conversation.setMc(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_MC)));
                conversations.add(conversation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (conversations.size() > 0) return conversations.get(0);
        else return null;
    }


    /**
     * 获取对话列表
     *
     * @param page
     * @return
     */
    @Override
    public List<Conversation> getConversationList(int page) {
        if (page < DEFAULT_PAGEE)
            page = DEFAULT_PAGEE;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                null, null, null, null,
                COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME + "  DESC", (page - 1) * DEFAULT_PAGESIZE + "," + DEFAULT_PAGESIZE);
        List<Conversation> conversations = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Conversation conversation = new Conversation();
                conversation.setCid(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_CID)));
                conversation.setType(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_TYPE)));
                conversation.setName(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_NAME)));
                conversation.setPair(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_PAIR)));
                conversation.setPwd(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_PWD)));
                conversation.setLast_message_time(cursor.getLong(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME)));
                conversation.setLast_message_text(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TEXT)));
                conversation.setUsids(cursor.getString(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_USIDS)));

                conversation.setIm_uid(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_IM_UID)));
                conversation.setIs_del(isDel(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_IS_DEL))));
                conversation.setDisa(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_DISABLE)));
                conversation.setMc(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_CONVERSATION_MC)));
                conversations.add(conversation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        int size = conversations.size();
        for (int i = 0; i < size; i++) {
            Message tmp = MessageDao.getInstance(context).getLastMessageByCid(conversations.get(i).getCid());
            if (tmp == null) continue;
            conversations.get(i).setLast_message_time(tmp.getCreate_time());
            conversations.get(i).setLast_message_text(tmp.getTxt());
            if (tmp.getExt() != null)
                conversations.get(i).setUsids(tmp.getExt().getZBUSID());
        }
        return conversations;
    }

    /**
     * 删除对话信息
     *
     * @param cid
     * @param type
     * @return
     */
    @Override
    public boolean delConversation(int cid, int type) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        int rows = 0;
        try {
            // Define 'where' part of query.
            String selection = ConversationDao.COLUMN_NAME_CONVERSATION_CID + " = ? and " + ConversationDao.COLUMN_NAME_CONVERSATION_TYPE + " = ? ";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(cid), String.valueOf(type)};
            // Issue SQL statement.
            rows = database.delete(TABLE_NAME, selection, selectionArgs);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        if (rows > 0) return true;
        else return false;


    }

    @Override
    public boolean hasConversation(int cid) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_NAME_CONVERSATION_CID + " = ?";
        Cursor cursor = database.rawQuery(sql,
                new String[]{String.valueOf(cid)});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;

    }

    @Override
    public boolean updateConversation(Conversation conversation) {
        if (conversation == null)
            throw new IllegalArgumentException("conversation can not be null");
        int rows = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            // update conversation set conversation = true where cid = cid
            ContentValues cv = new ContentValues();
            if (conversation.getType() != -1)
                cv.put(COLUMN_NAME_CONVERSATION_TYPE, conversation.getType());
            if (!TextUtils.isEmpty(conversation.getName()))
                cv.put(COLUMN_NAME_CONVERSATION_NAME, conversation.getName());

            if (!TextUtils.isEmpty(conversation.getPair()))
                cv.put(COLUMN_NAME_CONVERSATION_PAIR, conversation.getPair());
            if (!TextUtils.isEmpty(conversation.getPwd()))
                cv.put(COLUMN_NAME_CONVERSATION_PWD, conversation.getPwd());
            if (conversation.getLast_message_time() != 0)
                cv.put(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TIME, conversation.getLast_message_time());
            if (!TextUtils.isEmpty(conversation.getLast_message_text()))
                cv.put(COLUMN_NAME_CONVERSATION_LAST_MESSAGE_TEXT, conversation.getLast_message_text());
            if (!TextUtils.isEmpty(conversation.getUsids()))
                cv.put(COLUMN_NAME_CONVERSATION_USIDS, conversation.getUsids());

            cv.put(COLUMN_NAME_CONVERSATION_IM_UID, conversation.getIm_uid());
            cv.put(COLUMN_NAME_CONVERSATION_MC, conversation.getMc());
            cv.put(COLUMN_NAME_CONVERSATION_IS_DEL, isDel(conversation.is_del()));

            rows = database.update(TABLE_NAME,
                    cv,
                    COLUMN_NAME_CONVERSATION_CID + " = ?",
                    new String[]{conversation.getCid() + ""});
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


    private int isDel(boolean isDel) {
        if (isDel) return 1;
        return 0;
    }

    private boolean isDel(int isDel) {
        if (isDel == 1) return true;
        return false;
    }

    private int isDisable(boolean isDisable) {
        if (isDisable) return 1;
        return 0;
    }

    private boolean isDisable(int isDisable) {
        if (isDisable == 1) return true;
        return false;
    }

    @Override
    public void close() {
        mHelper.close();
    }
}

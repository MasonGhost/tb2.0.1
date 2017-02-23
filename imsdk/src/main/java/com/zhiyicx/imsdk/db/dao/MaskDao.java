package com.zhiyicx.imsdk.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhiyicx.imsdk.db.base.BaseDao;
import com.zhiyicx.imsdk.db.base.ZBSqlHelper;
import com.zhiyicx.imsdk.db.dao.soupport.MaskDaoSoupport;
import com.zhiyicx.imsdk.entity.Mask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungle on 16/8/12.
 * com.zhiyicx.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */
public class MaskDao extends BaseDao implements MaskDaoSoupport {
    public static final String TABLE_NAME = "mask";
    public static final String COLUMN_NAME_MASK_CID = "cid";
    public static final String COLUMN_NAME_MASK_ID = "id";
    public static final String COLUMN_NAME_MASK_FROM_IM_UID = "from_im_uid";
    public static final String COLUMN_NAME_MASK_TO_IM_UID = "to_im_uid";

    private volatile static MaskDao instance;

    private MaskDao(Context context) {
        this.context = context;
        mHelper = new ZBSqlHelper(context, DB_NAME, null, VERSION);
    }

    public static MaskDao getInstance(Context context) {

        if (instance == null) {
            synchronized (MaskDao.class) {
                if (instance == null) {
                    instance = new MaskDao(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close() {
        mHelper.close();
    }

    @Override
    public long insertMask(Mask mask) {
        if (mask == null) throw new IllegalArgumentException("mask can't be null");
        long resut = -1;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues map = new ContentValues();
            map.put(COLUMN_NAME_MASK_CID, mask.getCid());
            map.put(COLUMN_NAME_MASK_FROM_IM_UID, mask.getFrom_im_uid());
            map.put(COLUMN_NAME_MASK_TO_IM_UID, mask.getTo_im_uid());
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
     * 通过操作者的im_uid和对话cid，查询被操作者
     *
     * @param cid
     * @param from_im_uid
     * @return
     */
    @Override
    public List<Integer> getMaskedUid(int cid, int from_im_uid) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        Cursor cursor = database.query(
                TABLE_NAME, new String[]{"to_im_uid"},COLUMN_NAME_MASK_CID+" = ? and "+COLUMN_NAME_MASK_FROM_IM_UID+" = ?",new String[]{cid+"",from_im_uid+""}, null, null, null, null);
        List<Integer> toUids = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                toUids.add(cursor.getInt(cursor
                        .getColumnIndex(COLUMN_NAME_MASK_TO_IM_UID)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return toUids;
    }
}

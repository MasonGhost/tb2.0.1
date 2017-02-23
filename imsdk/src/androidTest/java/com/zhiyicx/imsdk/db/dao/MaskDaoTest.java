package com.zhiyicx.imsdk.db.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import com.zhiyicx.imsdk.entity.Mask;

import junit.framework.Assert;

import java.util.List;


/**
 * Created by jungle on 16/8/17.
 * com.zhiyicx.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */
public class MaskDaoTest extends AndroidTestCase {
    private MaskDao mMaskDao;

    @Override
    public void setUp() throws Exception {
        mMaskDao = MaskDao.getInstance(getContext());
    }

    @Override
    public void tearDown() throws Exception {
        mMaskDao.close();

    }

    public void testInsertMask() throws Exception {
        for (int i = 0; i < 10; i++) {
            Mask mask = new Mask();
            mask.setCid(1);
            mask.setFrom_im_uid(1);
            mask.setTo_im_uid(i);
            Assert.assertEquals(true, mMaskDao.insertMask(mask) > 0);
        }


    }

    public void testGetMaskedUid() throws Exception {
        List<Integer> touids = mMaskDao.getMaskedUid(1, 1);
        Log.i("touids", "touids --------= " + touids);
        Assert.assertEquals(true, touids.get(0) == 0);

    }


}
package com.zhiyicx.old.imsdk.db.dao;

import android.test.AndroidTestCase;
import android.util.Base64;
import android.util.Log;

import com.zhiyicx.old.imsdk.entity.Mask;

import junit.framework.Assert;

import java.net.URLEncoder;
import java.util.List;


/**
 * Created by jungle on 16/8/17.
 * com.zhiyicx.old.imsdk.db.dao
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
        Assert.assertEquals(true, touids.get(0) == 1);

    }

    public void testURLAndBase64Encode() {
        String name = "token_key,send_sns_token";
        String end = "dG9rZW5LZXklMkNzZW5kX3Nuc190b2tlbg==";
        String result = null;

        result = new String(Base64.encode(URLEncoder.encode(name).getBytes(), Base64.DEFAULT));

        System.out.println("------------result = " + result);
        Assert.assertEquals(end, result);


    }
}
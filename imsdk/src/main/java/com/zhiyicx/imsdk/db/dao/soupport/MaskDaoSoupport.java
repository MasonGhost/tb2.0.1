package com.zhiyicx.imsdk.db.dao.soupport;

import com.zhiyicx.imsdk.entity.Mask;

import java.util.List;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.imsdk.db.dao.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface MaskDaoSoupport {

    /**
     * 插入标记对话关系
     *
     * @param mask
     * @return
     */
    long insertMask(Mask mask);


    /**
     * 通过操作者的im_uid和对话cid，查询被操作者
     * @param cid
     * @param from_im_uid
     * @return
     */
    List<Integer> getMaskedUid(int cid,int from_im_uid);


}

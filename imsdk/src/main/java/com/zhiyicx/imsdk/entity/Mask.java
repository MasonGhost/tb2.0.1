package com.zhiyicx.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.imsdk.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class Mask implements Serializable{
    /**
     * CREATE TABLE `mask` (
     * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表ID',
     * `cid` int(11) NOT NULL COMMENT '对话ID',
     * `from_im_uid` int(11) NOT NULL COMMENT '操作者im_uid',
     * `to_im_uid` int(11) NOT NULL COMMENT '被操作者用户IM_uid',
     * PRIMARY KEY (`id`)
     * )
     */
    private int id;
    private int cid;
    private int from_im_uid;
    private int to_im_uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getFrom_im_uid() {
        return from_im_uid;
    }

    public void setFrom_im_uid(int from_im_uid) {
        this.from_im_uid = from_im_uid;
    }

    public int getTo_im_uid() {
        return to_im_uid;
    }

    public void setTo_im_uid(int to_im_uid) {
        this.to_im_uid = to_im_uid;
    }
}

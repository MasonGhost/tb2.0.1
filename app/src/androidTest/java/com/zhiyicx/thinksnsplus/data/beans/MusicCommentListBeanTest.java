package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MusicCommentListBeanTest extends AbstractDaoTestLongPk<MusicCommentListBeanDao, MusicCommentListBean> {

    public MusicCommentListBeanTest() {
        super(MusicCommentListBeanDao.class);
    }

    @Override
    protected MusicCommentListBean createEntity(Long key) {
        MusicCommentListBean entity = new MusicCommentListBean();
        entity.set_id(key);
        entity.setUser_id(15);
        entity.setReply_user(2);
        entity.setMusic_id(1);
        entity.setSpecial_id(1);
        entity.setState(1);
        return entity;
    }

}

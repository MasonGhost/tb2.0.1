package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DynamicCommentBeanTest extends AbstractDaoTestLongPk<DynamicCommentBeanDao, DynamicCommentBean> {

    public DynamicCommentBeanTest() {
        super(DynamicCommentBeanDao.class);
    }

    @Override
    protected DynamicCommentBean createEntity(Long key) {
        DynamicCommentBean entity = new DynamicCommentBean();
        entity.set_id(key);
        entity.setFeed_user_id(18);
        entity.setUser_id(33);
        entity.setReply_to_user_id(32);
        entity.setState(1);
        return entity;
    }

}

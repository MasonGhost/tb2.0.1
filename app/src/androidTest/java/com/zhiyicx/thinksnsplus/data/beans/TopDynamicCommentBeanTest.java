package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class TopDynamicCommentBeanTest extends AbstractDaoTestLongPk<TopDynamicCommentBeanDao, TopDynamicCommentBean> {

    public TopDynamicCommentBeanTest() {
        super(TopDynamicCommentBeanDao.class);
    }

    @Override
    protected TopDynamicCommentBean createEntity(Long key) {
        TopDynamicCommentBean entity = new TopDynamicCommentBean();
        entity.setId(key);
        entity.setId(12l);
        entity.setAmount(12);
        entity.setDay(12);
        entity.setUser_id(123);
        return entity;
    }

}

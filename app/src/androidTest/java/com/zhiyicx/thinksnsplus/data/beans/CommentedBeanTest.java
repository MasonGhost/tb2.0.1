package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class CommentedBeanTest extends AbstractDaoTestLongPk<CommentedBeanDao, CommentedBean> {

    public CommentedBeanTest() {
        super(CommentedBeanDao.class);
    }

    @Override
    protected CommentedBean createEntity(Long key) {
        CommentedBean entity = new CommentedBean();
        entity.setId(key);
        entity.setTarget_image(18L);
        return entity;
    }

}

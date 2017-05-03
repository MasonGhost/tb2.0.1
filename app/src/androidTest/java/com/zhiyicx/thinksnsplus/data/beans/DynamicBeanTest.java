package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DynamicBeanTest extends AbstractDaoTestLongPk<DynamicBeanDao, DynamicBean> {

    public DynamicBeanTest() {
        super(DynamicBeanDao.class);
    }

    @Override
    protected DynamicBean createEntity(Long key) {
        DynamicBean entity = new DynamicBean();
        entity.setId(key);
        entity.setUser_id(28);
        entity.setIsFollowed(true);
        entity.setState(1);
        return entity;
    }

}

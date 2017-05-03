package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DynamicDetailBeanTest extends AbstractDaoTestLongPk<DynamicDetailBeanDao, DynamicDetailBean> {

    public DynamicDetailBeanTest() {
        super(DynamicDetailBeanDao.class);
    }

    @Override
    protected DynamicDetailBean createEntity(Long key) {
        DynamicDetailBean entity = new DynamicDetailBean();
        entity.setFeed_mark(key);
        entity.setFeed_from(32);
        return entity;
    }

}

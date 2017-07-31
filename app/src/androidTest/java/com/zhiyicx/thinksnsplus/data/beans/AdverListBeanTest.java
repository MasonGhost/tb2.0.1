package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class AdverListBeanTest extends AbstractDaoTestLongPk<AdverListBeanDao, AllAdverListBean> {

    public AdverListBeanTest() {
        super(AdverListBeanDao.class);
    }

    @Override
    protected AllAdverListBean createEntity(Long key) {
        AllAdverListBean entity = new AllAdverListBean();
        entity.setId(key);
        return entity;
    }

}

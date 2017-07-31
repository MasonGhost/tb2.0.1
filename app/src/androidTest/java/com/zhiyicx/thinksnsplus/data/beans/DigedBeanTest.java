package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DigedBeanTest extends AbstractDaoTestLongPk<DigedBeanDao, DigedBean> {

    public DigedBeanTest() {
        super(DigedBeanDao.class);
    }

    @Override
    protected DigedBean createEntity(Long key) {
        DigedBean entity = new DigedBean();
        entity.setId(key);
        entity.setSource_cover(2L);
        return entity;
    }

}

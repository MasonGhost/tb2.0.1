package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class InfoTypeMyCatesBeanTest extends AbstractDaoTestLongPk<InfoTypeMyCatesBeanDao, InfoTypeCatesBean> {

    public InfoTypeMyCatesBeanTest() {
        super(InfoTypeMyCatesBeanDao.class);
    }

    @Override
    protected InfoTypeCatesBean createEntity(Long key) {
        InfoTypeCatesBean entity = new InfoTypeCatesBean();
        entity.set_id(key);
        return entity;
    }

}

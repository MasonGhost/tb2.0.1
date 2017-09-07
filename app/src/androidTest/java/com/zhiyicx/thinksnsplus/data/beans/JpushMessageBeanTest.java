package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class JpushMessageBeanTest extends AbstractDaoTestLongPk<JpushMessageBeanDao, JpushMessageBean> {

    public JpushMessageBeanTest() {
        super(JpushMessageBeanDao.class);
    }

    @Override
    protected JpushMessageBean createEntity(Long key) {
        JpushMessageBean entity = new JpushMessageBean();
        entity.setCreat_time(key);
        entity.setCreat_time(2414324);
        entity.setUser_id(3);
        entity.setIsNofity(false);
        return entity;
    }

}

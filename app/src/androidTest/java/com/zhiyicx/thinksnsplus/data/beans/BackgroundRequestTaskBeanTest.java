package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class BackgroundRequestTaskBeanTest extends AbstractDaoTestLongPk<BackgroundRequestTaskBeanDao, BackgroundRequestTaskBean> {

    public BackgroundRequestTaskBeanTest() {
        super(BackgroundRequestTaskBeanDao.class);
    }

    @Override
    protected BackgroundRequestTaskBean createEntity(Long key) {
        BackgroundRequestTaskBean entity = new BackgroundRequestTaskBean();
        entity.setBackgroundtask_id(key);
        entity.setMax_retry_count(10);
        return entity;
    }

}

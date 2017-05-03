package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class FlushMessagesTest extends AbstractDaoTestLongPk<FlushMessagesDao, FlushMessages> {

    public FlushMessagesTest() {
        super(FlushMessagesDao.class);
    }

    @Override
    protected FlushMessages createEntity(Long key) {
        FlushMessages entity = new FlushMessages();
        entity.setId(key);
        entity.setCount(16);
        entity.setMax_id(3);
        return entity;
    }

}

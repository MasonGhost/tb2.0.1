package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class ChannelSubscripBeanTest extends AbstractDaoTestLongPk<ChannelSubscripBeanDao, ChannelSubscripBean> {

    public ChannelSubscripBeanTest() {
        super(ChannelSubscripBeanDao.class);
    }

    @Override
    protected ChannelSubscripBean createEntity(Long key) {
        ChannelSubscripBean entity = new ChannelSubscripBean();
        entity.setKey(key);
        entity.setUserId(18);
        entity.setChannelSubscriped(true);
        entity.setId(18);
        return entity;
    }

}

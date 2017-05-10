package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class ChannelInfoBeanTest extends AbstractDaoTestLongPk<ChannelInfoBeanDao, ChannelInfoBean> {

    public ChannelInfoBeanTest() {
        super(ChannelInfoBeanDao.class);
    }

    @Override
    protected ChannelInfoBean createEntity(Long key) {
        ChannelInfoBean entity = new ChannelInfoBean();
        entity.setId(key);
        entity.setFollow_count(18);
        entity.setFeed_count(18);
        entity.setFollow_status(1);
        return entity;
    }

}

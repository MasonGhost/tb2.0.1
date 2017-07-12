package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class TopDynamicBeanTest extends AbstractDaoTestLongPk<TopDynamicDao, TopDynamicBean> {

    public TopDynamicBeanTest() {
        super(TopDynamicDao.class);
    }

    @Override
    protected TopDynamicBean createEntity(Long key) {
        TopDynamicBean entity = new TopDynamicBean();
        entity.setFeed_mark(key);
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class DynamicToolBeanTest extends AbstractDaoTestLongPk<DynamicToolBeanDao, DynamicToolBean> {

    public DynamicToolBeanTest() {
        super(DynamicToolBeanDao.class);
    }

    @Override
    protected DynamicToolBean createEntity(Long key) {
        DynamicToolBean entity = new DynamicToolBean();
        entity.setFeed_mark(key);
        entity.setFeed_digg_count(32);
        entity.setFeed_view_count(2);
        entity.setFeed_comment_count(123);
        entity.setIs_digg_feed(3);
        entity.setIs_collection_feed(1);
        return entity;
    }

}

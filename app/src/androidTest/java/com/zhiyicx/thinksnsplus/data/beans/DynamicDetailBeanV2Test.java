package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2Dao;

public class DynamicDetailBeanV2Test extends AbstractDaoTestLongPk<DynamicDetailBeanV2Dao, DynamicDetailBeanV2> {

    public DynamicDetailBeanV2Test() {
        super(DynamicDetailBeanV2Dao.class);
    }

    @Override
    protected DynamicDetailBeanV2 createEntity(Long key) {
        DynamicDetailBeanV2 entity = new DynamicDetailBeanV2();
        entity.setFeed_mark(key);
        entity.setId(0L);
        entity.setUser_id(0L);
        entity.setFeed_from(0);
        entity.setFeed_digg_count(0);
        entity.setFeed_view_count(0);
        entity.setFeed_comment_count(0);
        entity.setAudit_status(0);
        entity.setFeed_mark(0L);
        entity.setHas_digg(false);
        entity.setAmount(0);
        entity.setPaid(false);
        return entity;
    }

}

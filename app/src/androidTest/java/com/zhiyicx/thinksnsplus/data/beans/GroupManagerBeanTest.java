package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.GroupManagerBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupManagerBeanDao;

public class GroupManagerBeanTest extends AbstractDaoTestLongPk<GroupManagerBeanDao, GroupManagerBean> {

    public GroupManagerBeanTest() {
        super(GroupManagerBeanDao.class);
    }

    @Override
    protected GroupManagerBean createEntity(Long key) {
        GroupManagerBean entity = new GroupManagerBean();
        entity.set_id(key);
        entity.setGroup_id(1);
        entity.setUser_id(1);
        entity.setFounder(1);
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBeanDao;

public class GroupInfoBeanTest extends AbstractDaoTestLongPk<GroupInfoBeanDao, GroupInfoBean> {

    public GroupInfoBeanTest() {
        super(GroupInfoBeanDao.class);
    }

    @Override
    protected GroupInfoBean createEntity(Long key) {
        GroupInfoBean entity = new GroupInfoBean();
        entity.setId(key);
        entity.setId(1);
        entity.setIs_audit(1);
        entity.setPosts_count(1);
        entity.setMembers_count(1);
        entity.setIs_member(1);
        return entity;
    }

}

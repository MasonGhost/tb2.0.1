package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBeanDao;

public class GroupDynamicLikeListBeanTest extends AbstractDaoTestLongPk<GroupDynamicLikeListBeanDao, GroupDynamicLikeListBean> {

    public GroupDynamicLikeListBeanTest() {
        super(GroupDynamicLikeListBeanDao.class);
    }

    @Override
    protected GroupDynamicLikeListBean createEntity(Long key) {
        GroupDynamicLikeListBean entity = new GroupDynamicLikeListBean();
        entity.setId(key);
        entity.setUser_id();
        return entity;
    }

}

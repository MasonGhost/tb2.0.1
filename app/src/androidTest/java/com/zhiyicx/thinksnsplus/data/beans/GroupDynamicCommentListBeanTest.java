package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class GroupDynamicCommentListBeanTest extends AbstractDaoTestLongPk<GroupDynamicCommentListBeanDao, GroupDynamicCommentListBean> {

    public GroupDynamicCommentListBeanTest() {
        super(GroupDynamicCommentListBeanDao.class);
    }

    @Override
    protected GroupDynamicCommentListBean createEntity(Long key) {
        GroupDynamicCommentListBean entity = new GroupDynamicCommentListBean();
        entity.setId(key);
        entity.setUser_id(1L);
        entity.setReply_to_user_id(1L);
        entity.setTo_user_id(1);
        return entity;
    }

}

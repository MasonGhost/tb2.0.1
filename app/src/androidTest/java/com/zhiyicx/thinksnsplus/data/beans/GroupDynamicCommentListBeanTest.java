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
        entity.setUser_id(13);
        entity.setReply_to_user_id(156);
        entity.setTo_user_id(54);
        return entity;
    }

}

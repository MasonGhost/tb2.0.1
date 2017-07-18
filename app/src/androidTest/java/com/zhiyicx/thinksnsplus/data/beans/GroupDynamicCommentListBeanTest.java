package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBeanDao;

public class GroupDynamicCommentListBeanTest extends AbstractDaoTestLongPk<GroupDynamicCommentListBeanDao, GroupDynamicCommentListBean> {

    public GroupDynamicCommentListBeanTest() {
        super(GroupDynamicCommentListBeanDao.class);
    }

    @Override
    protected GroupDynamicCommentListBean createEntity(Long key) {
        GroupDynamicCommentListBean entity = new GroupDynamicCommentListBean();
        entity.setId(key);
        entity.setUser_id();
        entity.setReply_to_user_id();
        entity.setTo_user_id();
        return entity;
    }

}

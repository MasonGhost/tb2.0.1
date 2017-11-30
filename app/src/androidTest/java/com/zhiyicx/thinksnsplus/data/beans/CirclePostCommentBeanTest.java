package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBeanDao;

public class CirclePostCommentBeanTest extends AbstractDaoTestLongPk<CirclePostCommentBeanDao, CirclePostCommentBean> {

    public CirclePostCommentBeanTest() {
        super(CirclePostCommentBeanDao.class);
    }

    @Override
    protected CirclePostCommentBean createEntity(Long key) {
        CirclePostCommentBean entity = new CirclePostCommentBean();
        entity.setId(key);
        entity.setCircle_id();
        entity.setPost_id();
        entity.setUser_id();
        entity.setCommentable_id();
        entity.setReply_to_user_id();
        entity.setTo_user_id();
        entity.setState();
        return entity;
    }

}

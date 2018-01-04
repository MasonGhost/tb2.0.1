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
        entity.setCircle_id(1);
        entity.setPost_id(1);
        entity.setUser_id(1L);
        entity.setCommentable_id(1L);
        entity.setReply_to_user_id(1L);
        entity.setTo_user_id(1);
        entity.setState(1);
        return entity;
    }

}

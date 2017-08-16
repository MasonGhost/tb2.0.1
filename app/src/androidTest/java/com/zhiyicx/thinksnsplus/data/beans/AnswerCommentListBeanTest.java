package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBeanDao;

public class AnswerCommentListBeanTest extends AbstractDaoTestLongPk<AnswerCommentListBeanDao, AnswerCommentListBean> {

    public AnswerCommentListBeanTest() {
        super(AnswerCommentListBeanDao.class);
    }

    @Override
    protected AnswerCommentListBean createEntity(Long key) {
        AnswerCommentListBean entity = new AnswerCommentListBean();
        entity.setId(key);
        entity.setComment_mark(1L);
        entity.setCommentable_id(1);
        entity.setState(1);
        return entity;
    }

}

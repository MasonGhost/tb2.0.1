package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBeanDao;

public class QuestionCommentBeanTest extends AbstractDaoTestLongPk<QuestionCommentBeanDao, QuestionCommentBean> {

    public QuestionCommentBeanTest() {
        super(QuestionCommentBeanDao.class);
    }

    @Override
    protected QuestionCommentBean createEntity(Long key) {
        QuestionCommentBean entity = new QuestionCommentBean();
        entity.setId(key);
        entity.setComment_mark(0L);
        entity.setCommentable_id(1);
        entity.setState(1);
        return entity;
    }

}

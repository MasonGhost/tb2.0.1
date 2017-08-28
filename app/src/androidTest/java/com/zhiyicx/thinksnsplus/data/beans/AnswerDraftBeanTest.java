package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBeanDao;

public class AnswerDraftBeanTest extends AbstractDaoTestLongPk<AnswerDraftBeanDao, AnswerDraftBean> {

    public AnswerDraftBeanTest() {
        super(AnswerDraftBeanDao.class);
    }

    @Override
    protected AnswerDraftBean createEntity(Long key) {
        AnswerDraftBean entity = new AnswerDraftBean();
        entity.setId(key);
        entity.setAnonymity(1);
        return entity;
    }

}

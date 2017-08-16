package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBeanDao;

public class AnswerDigListBeanTest extends AbstractDaoTestLongPk<AnswerDigListBeanDao, AnswerDigListBean> {

    public AnswerDigListBeanTest() {
        super(AnswerDigListBeanDao.class);
    }

    @Override
    protected AnswerDigListBean createEntity(Long key) {
        AnswerDigListBean entity = new AnswerDigListBean();
        entity.setId(key);
        return entity;
    }

}

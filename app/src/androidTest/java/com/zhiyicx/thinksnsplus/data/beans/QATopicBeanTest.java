package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.beans.QATopicBeanDao;

public class QATopicBeanTest extends AbstractDaoTestLongPk<QATopicBeanDao, QATopicBean> {

    public QATopicBeanTest() {
        super(QATopicBeanDao.class);
    }

    @Override
    protected QATopicBean createEntity(Long key) {
        QATopicBean entity = new QATopicBean();
        entity.setId(key);
        entity.setId();
        entity.setQuestions_count();
        entity.setExperts_count();
        entity.setFollows_count();
        entity.setHas_follow();
        return entity;
    }

}

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
        entity.setQuestions_count(1);
        entity.setExperts_count(1);
        entity.setFollows_count(1);
        entity.setHas_follow(true);
        return entity;
    }

}

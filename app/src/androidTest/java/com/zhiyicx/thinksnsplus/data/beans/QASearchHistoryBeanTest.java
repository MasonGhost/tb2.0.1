package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.QASearchHistoryBeanDao;

public class QASearchHistoryBeanTest extends AbstractDaoTestLongPk<QASearchHistoryBeanDao, QASearchHistoryBean> {

    public QASearchHistoryBeanTest() {
        super(QASearchHistoryBeanDao.class);
    }

    @Override
    protected QASearchHistoryBean createEntity(Long key) {
        QASearchHistoryBean entity = new QASearchHistoryBean("",99);
        entity.setId(key);
        entity.setType(9);
        return entity;
    }

}

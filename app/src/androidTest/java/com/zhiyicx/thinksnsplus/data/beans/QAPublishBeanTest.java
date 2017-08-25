package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBeanDao;

public class QAPublishBeanTest extends AbstractDaoTestLongPk<QAPublishBeanDao, QAPublishBean> {

    public QAPublishBeanTest() {
        super(QAPublishBeanDao.class);
    }

    @Override
    protected QAPublishBean createEntity(Long key) {
        QAPublishBean entity = new QAPublishBean();
        entity.setId(key);
        entity.setAnonymity(1);
        entity.setAutomaticity(1);
        entity.setLook(1);
        entity.setAmount(1);
        return entity;
    }

}

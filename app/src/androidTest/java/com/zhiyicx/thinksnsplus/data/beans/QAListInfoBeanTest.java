package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.QAListInfoBeanDao;

public class QAListInfoBeanTest extends AbstractDaoTestLongPk<QAListInfoBeanDao, QAListInfoBean> {

    public QAListInfoBeanTest() {
        super(QAListInfoBeanDao.class);
    }

    @Override
    protected QAListInfoBean createEntity(Long key) {
        QAListInfoBean entity = new QAListInfoBean();
        entity.setId(key);
        entity.setAnonymity(1);
        entity.setAmount(1);
        entity.setAutomaticity(1);
        entity.setLook(1);
        entity.setExcellent(1);
        entity.setStatus(1);
        entity.setComments_count(1);
        entity.setAnswers_count(1);
        entity.setWatchers_count(1);
        entity.setLikes_count(1);
        entity.setViews_count(1);
        entity.setWatched(true);
        return entity;
    }

}

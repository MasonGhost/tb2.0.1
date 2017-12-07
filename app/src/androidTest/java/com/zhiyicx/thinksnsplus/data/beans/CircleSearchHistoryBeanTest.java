package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleSearchHistoryBeanDao;

public class CircleSearchHistoryBeanTest extends AbstractDaoTestLongPk<CircleSearchHistoryBeanDao, CircleSearchHistoryBean> {

    public CircleSearchHistoryBeanTest() {
        super(CircleSearchHistoryBeanDao.class);
    }

    @Override
    protected CircleSearchHistoryBean createEntity(Long key) {
        CircleSearchHistoryBean entity = new CircleSearchHistoryBean();
        entity.setId(key);
        entity.setCreate_time();
        entity.setType();
        return entity;
    }

}

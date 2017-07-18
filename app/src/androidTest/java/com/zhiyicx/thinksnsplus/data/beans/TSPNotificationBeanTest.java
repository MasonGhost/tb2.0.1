package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBeanDao;

public class TSPNotificationBeanTest extends AbstractDaoTestLongPk<TSPNotificationBeanDao, TSPNotificationBean> {

    public TSPNotificationBeanTest() {
        super(TSPNotificationBeanDao.class);
    }

    @Override
    protected TSPNotificationBean createEntity(Long key) {
        TSPNotificationBean entity = new TSPNotificationBean();
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicBeanDao;

public class TopDynamicBeanTest extends AbstractDaoTestLongPk<TopDynamicBeanDao, TopDynamicBean> {

    public TopDynamicBeanTest() {
        super(TopDynamicBeanDao.class);
    }

    @Override
    protected TopDynamicBean createEntity(Long key) {
        TopDynamicBean entity = new TopDynamicBean();
        entity.setType(1L);
        return entity;
    }

}

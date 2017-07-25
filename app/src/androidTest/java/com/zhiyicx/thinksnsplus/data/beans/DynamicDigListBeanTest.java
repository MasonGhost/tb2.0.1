package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBeanDao;

public class DynamicDigListBeanTest extends AbstractDaoTestLongPk<DynamicDigListBeanDao, DynamicDigListBean> {

    public DynamicDigListBeanTest() {
        super(DynamicDigListBeanDao.class);
    }

    @Override
    protected DynamicDigListBean createEntity(Long key) {
        DynamicDigListBean entity = new DynamicDigListBean();
        entity.setId(key);
        return entity;
    }

}

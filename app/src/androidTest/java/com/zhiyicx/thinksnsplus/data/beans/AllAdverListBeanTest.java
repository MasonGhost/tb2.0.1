package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBeanDao;

public class AllAdverListBeanTest extends AbstractDaoTestLongPk<AllAdverListBeanDao, AllAdverListBean> {

    public AllAdverListBeanTest() {
        super(AllAdverListBeanDao.class);
    }

    @Override
    protected AllAdverListBean createEntity(Long key) {
        AllAdverListBean entity = new AllAdverListBean();
        entity.setId(key);
        return entity;
    }

}

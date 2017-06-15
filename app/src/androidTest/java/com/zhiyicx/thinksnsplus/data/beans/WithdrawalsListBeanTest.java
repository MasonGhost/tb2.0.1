package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBeanDao;

public class WithdrawalsListBeanTest extends AbstractDaoTestLongPk<WithdrawalsListBeanDao, WithdrawalsListBean> {

    public WithdrawalsListBeanTest() {
        super(WithdrawalsListBeanDao.class);
    }

    @Override
    protected WithdrawalsListBean createEntity(Long key) {
        WithdrawalsListBean entity = new WithdrawalsListBean();
        entity.set_id(key);
        entity.setId(44);
        entity.setValue(2323);
        entity.setStatus(11);
        entity.setRemark("1111");
        return entity;
    }

}

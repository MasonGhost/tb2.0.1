package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBeanDao;

public class RechargeSuccessBeanTest extends AbstractDaoTestLongPk<RechargeSuccessBeanDao, RechargeSuccessBean> {

    public RechargeSuccessBeanTest() {
        super(RechargeSuccessBeanDao.class);
    }

    @Override
    protected RechargeSuccessBean createEntity(Long key) {
        RechargeSuccessBean entity = new RechargeSuccessBean();
        entity.setId();
        entity.setUser_id();
        entity.setAction();
        entity.setAmount();
        entity.setStatus();
        return entity;
    }

}

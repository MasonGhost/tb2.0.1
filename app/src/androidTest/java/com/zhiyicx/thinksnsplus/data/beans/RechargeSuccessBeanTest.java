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
        entity.setId(0);
        entity.setUser_id(3L);
        entity.setAction(1);
        entity.setAmount(1);
        entity.setStatus(1);
        return entity;
    }

}

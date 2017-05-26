package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class WalletBeanTest extends AbstractDaoTestLongPk<WalletBeanDao, WalletBean> {

    public WalletBeanTest() {
        super(WalletBeanDao.class);
    }

    @Override
    protected WalletBean createEntity(Long key) {
        WalletBean entity = new WalletBean();
        entity.setId(key);
        entity.setUser_id(123);
        entity.setBalance(1800.00);
        return entity;
    }

}

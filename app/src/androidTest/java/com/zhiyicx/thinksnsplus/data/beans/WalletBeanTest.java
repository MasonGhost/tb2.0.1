package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBeanDao;

public class WalletBeanTest extends AbstractDaoTestLongPk<WalletBeanDao, WalletBean> {

    public WalletBeanTest() {
        super(WalletBeanDao.class);
    }

    @Override
    protected WalletBean createEntity(Long key) {
        WalletBean entity = new WalletBean();
        entity.setId(key);
        entity.setUser_id();
        entity.setBalance();
        return entity;
    }

}

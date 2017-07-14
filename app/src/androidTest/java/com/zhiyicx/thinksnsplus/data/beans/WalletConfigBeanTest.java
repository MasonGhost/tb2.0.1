package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBeanDao;

public class WalletConfigBeanTest extends AbstractDaoTestLongPk<WalletConfigBeanDao, WalletConfigBean> {

    public WalletConfigBeanTest() {
        super(WalletConfigBeanDao.class);
    }

    @Override
    protected WalletConfigBean createEntity(Long key) {
        WalletConfigBean entity = new WalletConfigBean();
        entity.setUser_id(key);
        entity.setRatio(1);
        entity.setCase_min_amount(1);
        return entity;
    }

}

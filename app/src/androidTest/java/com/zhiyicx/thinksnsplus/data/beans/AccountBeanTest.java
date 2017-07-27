package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.data.beans.AccountBeanDao;

public class AccountBeanTest extends AbstractDaoTestLongPk<AccountBeanDao, AccountBean> {

    public AccountBeanTest() {
        super(AccountBeanDao.class);
    }

    @Override
    protected AccountBean createEntity(Long key) {
        AccountBean entity = new AccountBean();
        entity.setId(key);
        return entity;
    }

}

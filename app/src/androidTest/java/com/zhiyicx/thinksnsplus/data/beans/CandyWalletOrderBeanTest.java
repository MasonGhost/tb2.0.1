package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.CandyWalletOrderBean;
import com.zhiyicx.thinksnsplus.data.beans.CandyWalletOrderBeanDao;

public class CandyWalletOrderBeanTest extends AbstractDaoTestLongPk<CandyWalletOrderBeanDao, CandyWalletOrderBean> {

    public CandyWalletOrderBeanTest() {
        super(CandyWalletOrderBeanDao.class);
    }

    @Override
    protected CandyWalletOrderBean createEntity(Long key) {
        CandyWalletOrderBean entity = new CandyWalletOrderBean();
        entity.setId(key);
        entity.setCandy_cat_id();
        entity.setType();
        entity.setStatus();
        return entity;
    }

}

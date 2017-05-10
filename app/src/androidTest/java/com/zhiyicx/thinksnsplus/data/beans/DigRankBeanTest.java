package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBeanDao;

public class DigRankBeanTest extends AbstractDaoTestLongPk<DigRankBeanDao, DigRankBean> {

    public DigRankBeanTest() {
        super(DigRankBeanDao.class);
    }

    @Override
    protected DigRankBean createEntity(Long key) {
        DigRankBean entity = new DigRankBean();
        entity.setId(key);
        return entity;
    }

}

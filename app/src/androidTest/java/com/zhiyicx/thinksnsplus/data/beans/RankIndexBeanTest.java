package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBeanDao;

public class RankIndexBeanTest extends AbstractDaoTestLongPk<RankIndexBeanDao, RankIndexBean> {

    public RankIndexBeanTest() {
        super(RankIndexBeanDao.class);
    }

    @Override
    protected RankIndexBean createEntity(Long key) {
        RankIndexBean entity = new RankIndexBean();
        entity.setId(key);
        return entity;
    }

}

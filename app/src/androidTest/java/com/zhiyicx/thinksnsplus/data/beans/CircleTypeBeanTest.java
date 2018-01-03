package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBeanDao;

public class CircleTypeBeanTest extends AbstractDaoTestLongPk<CircleTypeBeanDao, CircleTypeBean> {

    public CircleTypeBeanTest() {
        super(CircleTypeBeanDao.class);
    }

    @Override
    protected CircleTypeBean createEntity(Long key) {
        CircleTypeBean entity = new CircleTypeBean();
        entity.setId(key);
        entity.setSort_by(10);
        return entity;
    }

}

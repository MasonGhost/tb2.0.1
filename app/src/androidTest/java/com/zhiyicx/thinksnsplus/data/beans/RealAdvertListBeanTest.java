package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBeanDao;

public class RealAdvertListBeanTest extends AbstractDaoTestLongPk<RealAdvertListBeanDao, RealAdvertListBean> {

    public RealAdvertListBeanTest() {
        super(RealAdvertListBeanDao.class);
    }

    @Override
    protected RealAdvertListBean createEntity(Long key) {
        RealAdvertListBean entity = new RealAdvertListBean();
        entity.setId(key);
        entity.setSpace_id(4L);
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBeanDao;

public class TbMessageBeanTest extends AbstractDaoTestLongPk<TbMessageBeanDao, TbMessageBean> {

    public TbMessageBeanTest() {
        super(TbMessageBeanDao.class);
    }

    @Override
    protected TbMessageBean createEntity(Long key) {
        TbMessageBean entity = new TbMessageBean();
        entity.set_id(key);
        entity.setMIsRead();
        return entity;
    }

}

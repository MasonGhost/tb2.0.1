package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2Dao;

public class SendDynamicDataBeanV2Test extends AbstractDaoTestLongPk<SendDynamicDataBeanV2Dao, SendDynamicDataBeanV2> {

    public SendDynamicDataBeanV2Test() {
        super(SendDynamicDataBeanV2Dao.class);
    }

    @Override
    protected SendDynamicDataBeanV2 createEntity(Long key) {
        SendDynamicDataBeanV2 entity = new SendDynamicDataBeanV2();
        entity.setId(key);
        return entity;
    }

}

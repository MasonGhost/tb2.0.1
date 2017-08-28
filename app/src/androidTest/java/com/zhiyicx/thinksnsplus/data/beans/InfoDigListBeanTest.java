package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBeanDao;

public class InfoDigListBeanTest extends AbstractDaoTestLongPk<InfoDigListBeanDao, InfoDigListBean> {

    public InfoDigListBeanTest() {
        super(InfoDigListBeanDao.class);
    }

    @Override
    protected InfoDigListBean createEntity(Long key) {
        InfoDigListBean entity = new InfoDigListBean();
        entity.setId(key);
        return entity;
    }

}

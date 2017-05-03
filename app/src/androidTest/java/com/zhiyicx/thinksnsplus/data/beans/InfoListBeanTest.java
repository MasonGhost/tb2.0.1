package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBeanDao;

public class InfoListBeanTest extends AbstractDaoTestLongPk<InfoListBeanDao, InfoListBean> {

    public InfoListBeanTest() {
        super(InfoListBeanDao.class);
    }

    @Override
    protected InfoListBean createEntity(Long key) {
        InfoListBean entity = new InfoListBean();
        entity.setInfo_type(key);
        return entity;
    }

}

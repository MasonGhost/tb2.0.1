package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class InfoListDataBeanTest extends AbstractDaoTestLongPk<InfoListDataBeanDao, InfoListDataBean> {

    public InfoListDataBeanTest() {
        super(InfoListDataBeanDao.class);
    }

    @Override
    protected InfoListDataBean createEntity(Long key) {
        InfoListDataBean entity = new InfoListDataBean();
        entity.setId(123L);
        entity.setIs_collection_news(432);
        entity.setIs_digg_news(21);
        return entity;
    }

}

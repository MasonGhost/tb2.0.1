package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class InfoRecommendBeanTest extends AbstractDaoTestLongPk<InfoRecommendBeanDao, InfoRecommendBean> {

    public InfoRecommendBeanTest() {
        super(InfoRecommendBeanDao.class);
    }

    @Override
    protected InfoRecommendBean createEntity(Long key) {
        InfoRecommendBean entity = new InfoRecommendBean();
        entity.set_id(key);
        entity.setId(3);
        entity.setCate_id(33);
        entity.setNews_id(1);
        entity.setSort(1);
        return entity;
    }

}

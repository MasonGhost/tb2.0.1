package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBeanDao;

public class InfoTypeCatesBeanTest extends AbstractDaoTestLongPk<InfoTypeCatesBeanDao, InfoTypeCatesBean> {

    public InfoTypeCatesBeanTest() {
        super(InfoTypeCatesBeanDao.class);
    }

    @Override
    protected InfoTypeCatesBean createEntity(Long key) {
        InfoTypeCatesBean entity = new InfoTypeCatesBean();
        entity.set_id(key);
        entity.setIsMyCate(true);
        return entity;
    }

}

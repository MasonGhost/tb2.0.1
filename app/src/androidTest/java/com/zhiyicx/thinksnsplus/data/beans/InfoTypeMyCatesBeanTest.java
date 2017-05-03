package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMyCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMyCatesBeanDao;

public class InfoTypeMyCatesBeanTest extends AbstractDaoTestLongPk<InfoTypeMyCatesBeanDao, InfoTypeMyCatesBean> {

    public InfoTypeMyCatesBeanTest() {
        super(InfoTypeMyCatesBeanDao.class);
    }

    @Override
    protected InfoTypeMyCatesBean createEntity(Long key) {
        InfoTypeMyCatesBean entity = new InfoTypeMyCatesBean();
        entity.set_id(key);
        return entity;
    }

}

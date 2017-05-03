package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBeanDao;

public class InfoTypeMoreCatesBeanTest extends AbstractDaoTestLongPk<InfoTypeMoreCatesBeanDao, InfoTypeMoreCatesBean> {

    public InfoTypeMoreCatesBeanTest() {
        super(InfoTypeMoreCatesBeanDao.class);
    }

    @Override
    protected InfoTypeMoreCatesBean createEntity(Long key) {
        InfoTypeMoreCatesBean entity = new InfoTypeMoreCatesBean();
        entity.set_id(key);
        return entity;
    }

}

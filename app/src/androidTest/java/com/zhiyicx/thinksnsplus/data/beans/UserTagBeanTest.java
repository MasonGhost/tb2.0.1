package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBeanDao;

public class UserTagBeanTest extends AbstractDaoTestLongPk<UserTagBeanDao, UserTagBean> {

    public UserTagBeanTest() {
        super(UserTagBeanDao.class);
    }

    @Override
    protected UserTagBean createEntity(Long key) {
        UserTagBean entity = new UserTagBean();
        entity.setId(key);
        entity.setTag_category_id(3l);
        return entity;
    }

}

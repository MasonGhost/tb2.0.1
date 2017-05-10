package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;

public class UserInfoBeanTest extends AbstractDaoTestLongPk<UserInfoBeanDao, UserInfoBean> {

    public UserInfoBeanTest() {
        super(UserInfoBeanDao.class);
    }

    @Override
    protected UserInfoBean createEntity(Long key) {
        UserInfoBean entity = new UserInfoBean();
        entity.setUser_id(key);
        return entity;
    }

}

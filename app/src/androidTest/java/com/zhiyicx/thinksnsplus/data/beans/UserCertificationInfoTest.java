package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfoDao;

public class UserCertificationInfoTest extends AbstractDaoTestLongPk<UserCertificationInfoDao, UserCertificationInfo> {

    public UserCertificationInfoTest() {
        super(UserCertificationInfoDao.class);
    }

    @Override
    protected UserCertificationInfo createEntity(Long key) {
        UserCertificationInfo entity = new UserCertificationInfo();
        entity.setId(key);
        entity.setId(1);
        entity.setUser_id(1);
        entity.setExaminer(1);
        entity.setStatus(1);
        return entity;
    }

}

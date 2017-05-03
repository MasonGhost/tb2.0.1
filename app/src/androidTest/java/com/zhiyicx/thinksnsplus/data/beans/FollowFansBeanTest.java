package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class FollowFansBeanTest extends AbstractDaoTestLongPk<FollowFansBeanDao, FollowFansBean> {

    public FollowFansBeanTest() {
        super(FollowFansBeanDao.class);
    }

    @Override
    protected FollowFansBean createEntity(Long key) {
        FollowFansBean entity = new FollowFansBean();
        entity.setId(key);
        entity.setOriginUserId(23);
        entity.setTargetUserId(32);
        entity.setOrigin_follow_status(1);
        entity.setTarget_follow_status(1);
        return entity;
    }

}

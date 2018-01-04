package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDao;

public class CircleInfoTest extends AbstractDaoTestLongPk<CircleInfoDao, CircleInfo> {

    public CircleInfoTest() {
        super(CircleInfoDao.class);
    }

    @Override
    protected CircleInfo createEntity(Long key) {
        CircleInfo entity = new CircleInfo();
        entity.setId(key);
        entity.setUser_id(1);
        entity.setCategory_id(1);
        entity.setAllow_feed(1);
        entity.setMoney(1);
        entity.setUsers_count(1);
        entity.setPosts_count(1);
        entity.setAudit(1);
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBeanDao;

public class CirclePostListBeanTest extends AbstractDaoTestLongPk<CirclePostListBeanDao, CirclePostListBean> {

    public CirclePostListBeanTest() {
        super(CirclePostListBeanDao.class);
    }

    @Override
    protected CirclePostListBean createEntity(Long key) {
        CirclePostListBean entity = new CirclePostListBean();
        entity.setId(key);
        entity.setGroup_id(1);
        entity.setLikes_count(1);
        entity.setComments_count(1);
        entity.setViews_count(1);
        entity.setLiked(false);
        entity.setCollected(false);
        entity.setState(1);
        return entity;
    }

}

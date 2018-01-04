package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBeanDao;

public class PostDigListBeanTest extends AbstractDaoTestLongPk<PostDigListBeanDao, PostDigListBean> {

    public PostDigListBeanTest() {
        super(PostDigListBeanDao.class);
    }

    @Override
    protected PostDigListBean createEntity(Long key) {
        PostDigListBean entity = new PostDigListBean();
        entity.setId(key);
        return entity;
    }

}

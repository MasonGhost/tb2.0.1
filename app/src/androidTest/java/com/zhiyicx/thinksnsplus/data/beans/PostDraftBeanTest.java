package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBeanDao;

public class PostDraftBeanTest extends AbstractDaoTestLongPk<PostDraftBeanDao, PostDraftBean> {

    public PostDraftBeanTest() {
        super(PostDraftBeanDao.class);
    }

    @Override
    protected PostDraftBean createEntity(Long key) {
        PostDraftBean entity = new PostDraftBean();
        entity.setMark(key);
        entity.setIsOutCircle();
        return entity;
    }

}

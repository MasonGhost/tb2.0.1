package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBeanDao;

public class TopDynamicCommentBeanTest extends AbstractDaoTestLongPk<TopDynamicCommentBeanDao, TopDynamicCommentBean> {

    public TopDynamicCommentBeanTest() {
        super(TopDynamicCommentBeanDao.class);
    }

    @Override
    protected TopDynamicCommentBean createEntity(Long key) {
        TopDynamicCommentBean entity = new TopDynamicCommentBean();
        entity.setId(key);
        entity.setId();
        entity.setAmount();
        entity.setDay();
        entity.setUser_id();
        return entity;
    }

}

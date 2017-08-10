package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class InfoCommentListBeanTest extends AbstractDaoTestLongPk<InfoCommentListBeanDao, InfoCommentListBean> {

    public InfoCommentListBeanTest() {
        super(InfoCommentListBeanDao.class);
    }

    @Override
    protected InfoCommentListBean createEntity(Long key) {
        InfoCommentListBean entity = new InfoCommentListBean();
        entity.setId(32L);
        entity.setInfo_id(323);
        entity.setUser_id(11);
        entity.setReply_to_user_id(44);
        entity.setComment_mark(4214324324324324l);
        entity.setState(1);
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBeanDao;

public class MusicCommentListBeanTest extends AbstractDaoTestLongPk<MusicCommentListBeanDao, MusicCommentListBean> {

    public MusicCommentListBeanTest() {
        super(MusicCommentListBeanDao.class);
    }

    @Override
    protected MusicCommentListBean createEntity(Long key) {
        MusicCommentListBean entity = new MusicCommentListBean();
        entity.set_id(key);
        entity.setComment_id();
        entity.setUser_id();
        entity.setReply_to_user_id();
        entity.setMusic_id();
        entity.setSpecial_id();
        entity.setState();
        return entity;
    }

}

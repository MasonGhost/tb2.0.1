package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBeanDao;

public class ChatGroupBeanTest extends AbstractDaoTestLongPk<ChatGroupBeanDao, ChatGroupBean> {

    public ChatGroupBeanTest() {
        super(ChatGroupBeanDao.class);
    }

    @Override
    protected ChatGroupBean createEntity(Long key) {
        ChatGroupBean entity = new ChatGroupBean();
        entity.setKey(key);
        entity.setMembersonly();
        entity.setAllowinvites();
        entity.setMaxusers();
        entity.setOwner();
        entity.setAffiliations_count();
        entity.setIsPublic();
        return entity;
    }

}

package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBeanDao;

public class AnswerInfoBeanTest extends AbstractDaoTestLongPk<AnswerInfoBeanDao, AnswerInfoBean> {

    public AnswerInfoBeanTest() {
        super(AnswerInfoBeanDao.class);
    }

    @Override
    protected AnswerInfoBean createEntity(Long key) {
        AnswerInfoBean entity = new AnswerInfoBean();
        entity.setId(key);
        entity.setAnonymity();
        entity.setAdoption();
        entity.setInvited();
        entity.setComments_count();
        entity.setRewards_amount();
        entity.setRewarder_count();
        entity.setLikes_count();
        entity.setViews_count();
        entity.setLiked();
        entity.setCollected();
        entity.setRewarded();
        return entity;
    }

}

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
        entity.setAnonymity(1);
        entity.setAdoption(1);
        entity.setInvited(1);
        entity.setComments_count(1);
        entity.setRewards_amount(1);
        entity.setRewarder_count(1);
        entity.setLikes_count(1);
        entity.setViews_count(1);
        entity.setLiked(false);
        entity.setCollected(false);
        entity.setRewarded(false);
        return entity;
    }

}

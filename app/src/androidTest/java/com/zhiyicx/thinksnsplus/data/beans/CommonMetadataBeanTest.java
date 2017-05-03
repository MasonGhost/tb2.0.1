package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.thinksnsplus.comment.CommonMetadataBean;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class CommonMetadataBeanTest extends AbstractDaoTestLongPk<CommonMetadataBeanDao, CommonMetadataBean> {

    public CommonMetadataBeanTest() {
        super(CommonMetadataBeanDao.class);
    }

    @Override
    protected CommonMetadataBean createEntity(Long key) {
        CommonMetadataBean entity = new CommonMetadataBean();
        entity.set_id(key);
        entity.setComment_id(18);
        entity.setComment_type(1);
        entity.setSource_id(18);
        entity.setComment_state(11);
        entity.setTo_user(128);
        entity.setFrom_user(38);
        return entity;
    }

}

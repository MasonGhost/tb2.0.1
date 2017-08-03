package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBeanDao;

public class TagCategoryBeanTest extends AbstractDaoTestLongPk<TagCategoryBeanDao, TagCategoryBean> {

    public TagCategoryBeanTest() {
        super(TagCategoryBeanDao.class);
    }

    @Override
    protected TagCategoryBean createEntity(Long key) {
        TagCategoryBean entity = new TagCategoryBean();
        entity.setId(key);
        return entity;
    }

}

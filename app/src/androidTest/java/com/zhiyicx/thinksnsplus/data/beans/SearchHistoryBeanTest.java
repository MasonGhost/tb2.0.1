package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.zhiyicx.thinksnsplus.modules.tb.search.SearchHistoryBean;

public class SearchHistoryBeanTest extends AbstractDaoTestLongPk<SearchHistoryBeanDao, SearchHistoryBean> {

    public SearchHistoryBeanTest() {
        super(SearchHistoryBeanDao.class);
    }

    @Override
    protected SearchHistoryBean createEntity(Long key) {
        SearchHistoryBean entity = new SearchHistoryBean();
        entity.setId(key);
        entity.setCreate_time();
        return entity;
    }

}

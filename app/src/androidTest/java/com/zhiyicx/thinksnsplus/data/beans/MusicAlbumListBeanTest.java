package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MusicAlbumListBeanTest extends AbstractDaoTestLongPk<MusicAlbumListBeanDao, MusicAlbumListBean> {

    public MusicAlbumListBeanTest() {
        super(MusicAlbumListBeanDao.class);
    }

    @Override
    protected MusicAlbumListBean createEntity(Long key) {
        MusicAlbumListBean entity = new MusicAlbumListBean();
        entity.set_id(key);
        entity.setId(1);
        entity.setTaste_count(22);
        entity.setShare_count(213);
        entity.setComment_count(12);
        entity.setCollect_count(32);
        entity.setHas_collect(true);
        return entity;
    }

}

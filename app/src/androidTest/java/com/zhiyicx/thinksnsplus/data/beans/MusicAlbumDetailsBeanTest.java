package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MusicAlbumDetailsBeanTest extends AbstractDaoTestLongPk<MusicAlbumDetailsBeanDao, MusicAlbumDetailsBean> {

    public MusicAlbumDetailsBeanTest() {
        super(MusicAlbumDetailsBeanDao.class);
    }

    @Override
    protected MusicAlbumDetailsBean createEntity(Long key) {
        MusicAlbumDetailsBean entity = new MusicAlbumDetailsBean();
        entity.setId(key);
        entity.setId(1L);
        entity.setTaste_count(2);
        entity.setShare_count(2);
        entity.setComment_count(2);
        entity.setCollect_count(2);
        entity.setHas_collect(false);
        return entity;
    }

}

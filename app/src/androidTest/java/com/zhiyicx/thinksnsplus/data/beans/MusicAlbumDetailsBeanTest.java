package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MusicAlbumDetailsBeanTest extends AbstractDaoTestLongPk<MusicAlbumDetailsBeanDao, MusicAlbumDetailsBean> {

    public MusicAlbumDetailsBeanTest() {
        super(MusicAlbumDetailsBeanDao.class);
    }

    @Override
    protected MusicAlbumDetailsBean createEntity(Long key) {
        MusicAlbumDetailsBean entity = new MusicAlbumDetailsBean();
        entity.setId(13);
        entity.setTaste_count(33);
        entity.setShare_count(443);
        entity.setComment_count(34);
        entity.setCollect_count(11);
        entity.setIs_collection(1);
        return entity;
    }

}

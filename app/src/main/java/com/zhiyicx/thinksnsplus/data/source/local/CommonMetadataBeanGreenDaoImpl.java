package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment.CommonMetadataBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment.CommonMetadataBeanDao;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/18:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CommonMetadataBeanGreenDaoImpl extends CommonCacheImpl<CommonMetadataBean> {

    private CommonMetadataBeanDao mCommonMetadataBeanDao;

    public CommonMetadataBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(CommonMetadataBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<CommonMetadataBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CommonMetadataBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<CommonMetadataBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(CommonMetadataBean dta) {

    }

    @Override
    public void updateSingleData(CommonMetadataBean newData) {

    }

    @Override
    public long insertOrReplace(CommonMetadataBean newData) {
        return 0;
    }
}

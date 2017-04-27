package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import android.app.Application;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.data.source.local.CommonMetadataBeanGreenDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/15:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class CommonMetadataProvider<T> implements ICommonMetadataProvider {

    private List<T> mComments;
    private List<CommonMetadataBean> mCommonComments;

    protected CommonMetadataBeanGreenDaoImpl mCommonMetadataBeanGreenDao;

    public CommonMetadataProvider(List<T> comments) {
        mComments = comments;
        mCommonMetadataBeanGreenDao = new CommonMetadataBeanGreenDaoImpl((Application) BaseApplication.getContext());
    }

    @Override
    public List<CommonMetadata> iterator() {
        ArrayList<CommonMetadata> tracks = new ArrayList<>();
        mCommonComments = new ArrayList<>();
        if (mComments == null || mComments.isEmpty()) {
            return tracks;
        }
        for (T data : mComments) {
            mCommonComments.add(buildCommonMetadataBean(data));
            tracks.add(buildCommonMetadata(data));
        }
        saveCommonMetadataList(mCommonComments);
        return tracks;
    }

    public void setComments(List<T> comments) {
        mComments = comments;
    }

    public List<CommonMetadataBean> getLocalCommonComments(int type, int sourceId) {
        return mCommonMetadataBeanGreenDao.getCommonMetadataListByTypeAndID(type, sourceId);
    }

    public List<T> getCacheCommonComments(int type, int sourceId) {
        List<T> needDatas=new ArrayList<>();
        List<CommonMetadataBean> cacheDatas=mCommonMetadataBeanGreenDao.getCommonMetadataListByTypeAndID(type, sourceId);
        for (CommonMetadataBean data:cacheDatas){
            needDatas.add(buildCommonData(data));
        }
        return needDatas;
    }

    public void insertOrReplaceOne(CommonMetadataBean commonMetadataBean){
        mCommonMetadataBeanGreenDao.insertOrReplace(commonMetadataBean);
    }

    public void deleteOne(CommonMetadataBean commonMetadataBean){
        mCommonMetadataBeanGreenDao.deleteSingleCache(commonMetadataBean);
    }

    public abstract CommonMetadata buildCommonMetadata(T commentData);

    public abstract T buildCommonData(CommonMetadataBean commonMetadataBean);

    public abstract CommonMetadataBean buildCommonMetadataBean(T commentData);

    public abstract void saveCommonMetadataList(List<CommonMetadataBean> commonComments);
}

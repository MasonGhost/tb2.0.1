package com.zhiyicx.thinksnsplus.comment;

import android.app.Application;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.data.source.local.CommonMetadataBeanGreenDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/15:36
 * @Email Jliuer@aliyun.com
 * @Description 评论内容处理基类
 */
public abstract class CommonMetadataProvider<T> implements ICommonMetadataProvider {

    private List<T> mComments;
    private List<CommonMetadataBean> mCommonComments;
    protected int[] mStates;

    protected CommonMetadataBeanGreenDaoImpl mCommonMetadataBeanGreenDao;


    public CommonMetadataProvider(List<T> comments) {
        mComments = comments;
        mStates = new int[]{CommonMetadataBean.SEND_ING, CommonMetadataBean.SEND_SUCCESS, CommonMetadataBean.SEND_ERROR};
        mCommonMetadataBeanGreenDao = new CommonMetadataBeanGreenDaoImpl((Application) BaseApplication.getContext());
    }

    @Override
    public List<CommonMetadata> convertAndSave() {
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

    public CommonMetadataProvider setComments(List<T> comments) {
        mComments = comments;
        return this;
    }

    public List<CommonMetadataBean> getLocalCommonComments(int type, int sourceId) {
        return mCommonMetadataBeanGreenDao.getCommonMetadataListByTypeAndID(type, sourceId);
    }

    public List<T> getCacheCommonComments(int type, int sourceId) {
        List<T> needDatas = new ArrayList<>();
        List<CommonMetadataBean> cacheDatas = mCommonMetadataBeanGreenDao.getCommonMetadataListByTypeAndID(type, sourceId);
        for (CommonMetadataBean data : cacheDatas) {
            needDatas.add(buildRealNeedData(data));
        }
        return needDatas;
    }

    public void insertOrReplaceOne(CommonMetadataBean commonMetadataBean) {
        mCommonMetadataBeanGreenDao.insertOrReplace(commonMetadataBean);
    }

    public CommonMetadataBean getCommentByCommentMark(long mark) {
        return mCommonMetadataBeanGreenDao.getCommonMetadataListByCommentMark(mark);
    }

    public void deleteOne(CommonMetadataBean commonMetadataBean) {
        mCommonMetadataBeanGreenDao.deleteSingleCache(commonMetadataBean);
    }

    public void saveCommonMetadataList(List<CommonMetadataBean> commonComments) {
        mCommonMetadataBeanGreenDao.saveMultiData(commonComments);
    }

    public abstract CommonMetadata buildCommonMetadata(T commentData);

    public abstract T buildRealNeedData(CommonMetadataBean commonMetadataBean);

    public abstract CommonMetadataBean buildCommonMetadataBean(T commentData);

}

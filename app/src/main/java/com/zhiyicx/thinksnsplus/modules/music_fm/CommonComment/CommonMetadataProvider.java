package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import java.util.ArrayList;
import java.util.Iterator;
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

    public CommonMetadataProvider(List<T> comments) {
        mComments = comments;
    }

    @Override
    public List<CommonMetadata> iterator() {
        ArrayList<CommonMetadata> tracks = new ArrayList<>();
        mCommonComments = new ArrayList<>();
        if (mComments == null) {
            return tracks;
        }
        for (T data : mComments) {
            mCommonComments.add(buildCommonMetadataBean(data));
            tracks.add(buildCommonMetadata(data));
        }
        return tracks;
    }

    protected abstract CommonMetadata buildCommonMetadata(T commentData);

    protected abstract CommonMetadataBean buildCommonMetadataBean(T commentData);
}

package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBaseCircleRepository {
    Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet);

    Observable<BaseJsonV2<CircleInfo>> createCircle(long categoryId, Map<String, Object> params, Map<String, String> filePathList);

    Observable<BaseJsonV2<Object>> sendCirclePost(PostPublishBean publishBean);

    Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId);

    Observable<List<CircleInfo>> getMyJoinedCircle(int limit, int offet);

    Observable<List<CircleInfo>> getAllCircle(int limit, int offet);

    Observable<BaseJsonV2<Integer>> getCircleCount();

}

package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBaseCircleRepository {
    Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet);
}

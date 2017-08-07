package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/7
 * @contact email:648129313@qq.com
 */

public interface IBaseInfoRepository {

    // 搜索也用这个接口
    Observable<List<InfoListDataBean>> getInfoListV2(String cate_id, String key, long max_id,
                                                     long page, int isRecommend);
}

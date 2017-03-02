package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public interface IDynamicReppsitory {
    /**
     * publish dynamic
     *
     * @param dynamicDetailBean dynamic content
     * @return basejson, object is null
     */
    Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean);

    /**
     * get dynamic list
     *
     * @param type   "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id 用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param page   页码 热门选填
     * @return dynamic list
     */
    Observable<BaseJson<List<DynamicBean>>> getDynamicList(String type, Long max_id, int page);

    /**
     * 动态点赞
     *
     * @param feed_id
     * @return
     */
    Observable<BaseJson<String>> likeDynamic(Long feed_id);

    /**
     * 取消动态点赞
     *
     * @param feed_id
     * @return
     */
    Observable<BaseJson<String>> cancleLikeDynamic(Long feed_id);

    /**
     * 动态收藏
     *
     * @param feed_id
     * @return
     */
    Observable<BaseJson<String>> collectDynamic(Long feed_id);


    /**
     * 取消动态收藏
     */
    Observable<BaseJson<String>> cancleCollectDynamic(Long feed_id);

    /**
     * 获取动态点赞列表
     */
    Observable<BaseJson<List<UserInfoBean>>> getDynamicDigList(Long feed_id, Integer max_id);
}

package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public interface IDynamicReppsitory {
    /**
     *  publish dynamic
     * @param dynamicDetailBean dynamic content
     * @return  basejson,object is null
     */
    Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean);
}

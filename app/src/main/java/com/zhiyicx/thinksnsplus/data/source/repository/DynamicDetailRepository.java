package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailRepository extends BaseDynamicRepository implements DynamicDetailContract.Repository {

    public DynamicDetailRepository(ServiceManager serviceManager, Context context) {
        super(serviceManager, context);
    }
}

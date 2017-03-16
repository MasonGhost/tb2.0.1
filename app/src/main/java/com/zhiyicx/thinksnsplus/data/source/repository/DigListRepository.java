package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListContract;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListRepository extends BaseDynamicRepository implements DigListContract.Repository {
    public DigListRepository(ServiceManager serviceManager, Context context) {
        super(serviceManager, context);
    }
}

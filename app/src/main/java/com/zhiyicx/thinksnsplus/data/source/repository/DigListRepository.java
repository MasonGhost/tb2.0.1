package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListRepository extends BaseDynamicRepository implements DigListContract.Repository {

    @Inject
    public DigListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
    
}

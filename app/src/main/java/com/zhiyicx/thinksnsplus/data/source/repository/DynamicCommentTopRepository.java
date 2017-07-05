package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollContract;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTopRepository implements DynamicCommentTopContract.Repository {

    DynamicClient mDynamicClient;


    @Inject
    public DynamicCommentTopRepository(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(long feed_id, int amount, int day) {
        return null;
    }
}

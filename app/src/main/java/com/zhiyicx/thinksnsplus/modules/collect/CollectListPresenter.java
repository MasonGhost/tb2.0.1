package com.zhiyicx.thinksnsplus.modules.collect;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectListPresenter extends BasePresenter<CollectListContract.View> implements CollectListContract.Presenter {
    @Inject
    public CollectListPresenter(CollectListContract.View rootView) {
        super(rootView);
    }
}

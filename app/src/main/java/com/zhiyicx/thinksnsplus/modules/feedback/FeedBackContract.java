package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/06/02/17:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface FeedBackContract {
    interface View extends IBaseView<Presenter>{}

    interface Presenter extends IBasePresenter{}

    interface Repository{}
}

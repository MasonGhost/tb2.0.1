package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishContentConstact {
    interface View extends IBaseView<Presenter>{}
    interface Presenter extends IBaseTouristPresenter{}
    interface Repository{}
}

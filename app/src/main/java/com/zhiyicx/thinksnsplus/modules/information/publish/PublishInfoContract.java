package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishInfoContract {
    interface View extends IBaseView<Presenter>{}
    interface Presenter extends IBasePresenter{}
    interface Repository{}
}

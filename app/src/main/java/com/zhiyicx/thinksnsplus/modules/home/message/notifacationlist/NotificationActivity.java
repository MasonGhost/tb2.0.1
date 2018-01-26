package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */

public class NotificationActivity extends TSActivity<NotificationPresenter, NotificationFragment>{

    @Override
    protected NotificationFragment getFragment() {
        return NotificationFragment.instance();
    }

    @Override
    protected void componentInject() {
        DaggerNotificationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .notificationPresenterModule(new NotificationPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}

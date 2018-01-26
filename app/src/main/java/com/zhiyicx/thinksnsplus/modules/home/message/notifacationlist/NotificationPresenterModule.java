package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.thinksnsplus.data.source.repository.NotificationRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@Module
public class NotificationPresenterModule {

    private NotificationContract.View mView;

    public NotificationPresenterModule(NotificationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NotificationContract.View provideNotificationContractView(){
        return mView;
    }

}

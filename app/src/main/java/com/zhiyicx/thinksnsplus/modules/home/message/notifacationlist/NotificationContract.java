package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public interface NotificationContract {

    interface View extends ITSListView<TSPNotificationBean, Presenter>{

    }

    interface Presenter extends ITSListPresenter<TSPNotificationBean>{

        void readNotification();
    }

    interface Repository{
        Observable<List<TSPNotificationBean>> getNotificationList(int size);
    }

}

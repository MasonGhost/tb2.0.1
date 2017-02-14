package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public interface FollowFansListContract {
    interface View extends ITSListView<FollowFansItemBean,Presenter>{

    }
    interface Presenter extends ITSListPresenter<FollowFansItemBean>{

    }
    interface Repository {

    }

}

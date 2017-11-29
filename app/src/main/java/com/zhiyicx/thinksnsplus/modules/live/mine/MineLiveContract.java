package com.zhiyicx.thinksnsplus.modules.live.mine;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public interface MineLiveContract {
    interface View extends ITSListView<UserInfoBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {

    }


}

package com.zhiyicx.thinksnsplus.modules.channel;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelListContract {
    interface View extends ITSListView<ChannelSubscripBean, ChannelListContract.Presenter> {

    }

    interface Presenter extends ITSListPresenter<ChannelSubscripBean> {

    }

    interface Repository {

    }
}

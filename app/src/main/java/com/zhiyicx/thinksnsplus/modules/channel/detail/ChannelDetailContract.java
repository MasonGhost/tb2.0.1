package com.zhiyicx.thinksnsplus.modules.channel.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseChannelRepository;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public interface ChannelDetailContract {
    interface View extends ITSListView<DynamicBean, Presenter> {

    }

    interface Repository extends IBaseChannelRepository {

    }

    interface Presenter extends ITSListPresenter<DynamicBean> {

    }
}

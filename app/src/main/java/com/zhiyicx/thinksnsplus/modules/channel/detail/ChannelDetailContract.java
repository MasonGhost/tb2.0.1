package com.zhiyicx.thinksnsplus.modules.channel.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseChannelRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public interface ChannelDetailContract {
    interface View extends ITSListView<DynamicBean, Presenter> {
        /**
         * 所有接口都请求完毕后回调
         */
        void allDataReady();

        /**
         * 加载失败
         */
        void loadAllError();

        /**
         * 获取频道id
         * @return
         */
        long getChannelId();
    }

    interface Repository extends IBaseChannelRepository {

    }

    interface Presenter extends DynamicContract.Presenter {

    }
}

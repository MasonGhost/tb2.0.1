package com.zhiyicx.thinksnsplus.modules.channel.mine;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/25
 * @contact email:648129313@qq.com
 */

public interface MyGroupContract {

    interface View extends ITSListView<GroupInfoBean, Presenter>{

    }
    interface Presenter extends ITSListPresenter<GroupInfoBean>{

    }
    interface Repository extends IBaseChannelRepository{

    }
}

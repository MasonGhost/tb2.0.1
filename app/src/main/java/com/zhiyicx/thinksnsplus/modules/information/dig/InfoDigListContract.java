package com.zhiyicx.thinksnsplus.modules.information.dig;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseInfoRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */

public interface InfoDigListContract {

    interface View extends ITSListView<InfoDigListBean, Presenter>{
        long getNesId();
        void upDataFollowState(int position);
    }

    interface Presenter extends ITSListPresenter<InfoDigListBean>{
        void handleFollowUser(int position, UserInfoBean followFansBean);
    }

    interface Repository extends IBaseInfoRepository{

    }
}

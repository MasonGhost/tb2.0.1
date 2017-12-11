package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @author Jliuer
 * @Date 17/12/11 15:59
 * @Email Jliuer@aliyun.com
 * @Description 
 */
public interface DigListContract {

    interface View extends ITSListView<BaseListBean, Presenter> {
        long getSourceId();
        String getType();
        void upDataFollowState(int position);
    }

    interface Presenter extends ITSListPresenter<BaseListBean> {
        void handleFollowUser(int position, UserInfoBean followFansBean);
    }

    interface Repository {

    }
}

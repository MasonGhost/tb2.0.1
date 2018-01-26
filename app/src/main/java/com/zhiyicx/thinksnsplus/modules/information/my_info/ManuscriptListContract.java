package com.zhiyicx.thinksnsplus.modules.information.my_info;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseInfoRepository;

/**
 * @Author Jliuer
 * @Date 2017/08/23/11:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface ManuscriptListContract {

    interface View extends ITSListView<InfoListDataBean, Presenter> {
        String getMyInfoType();
    }

    interface Presenter extends ITSListPresenter<InfoListDataBean> {
    }

}

package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface AddInfoContract {
    interface View extends IBaseView<Presenter> {
        void setInfoType(List<InfoTypeCatesBean> infoType);
    }

    interface Presenter extends IBasePresenter {
        List<InfoTypeCatesBean> getInfoTypeBean();
        void getInfoType();
    }

}

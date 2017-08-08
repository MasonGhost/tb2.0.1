package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchContract;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface LocationSearchContract {

    interface View extends ITSListView<LocationBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<LocationBean> {

        void searchLocation(String name);

    }

    interface Repository {

    }
}

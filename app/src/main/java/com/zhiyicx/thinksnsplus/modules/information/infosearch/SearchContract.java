package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface SearchContract {

    interface View extends ITSListView<InfoListBean.ListBean,Presenter>{
        String getKeyWords();
    }

    interface Presenter extends ITSListPresenter<InfoListBean.ListBean>{

    }

    interface Repository{
        Observable<BaseJson<List<InfoListBean.ListBean>>> searchInfoList(String key,
                                                                         long max_id);
    }


}

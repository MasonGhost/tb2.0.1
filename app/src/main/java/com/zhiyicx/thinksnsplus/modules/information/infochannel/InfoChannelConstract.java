package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoChannelConstract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBasePresenter{
        void doSubscribe(String follows);
        void handleSubscribe(String follows);
        void updateLocalInfoType(InfoTypeBean infoTypeBean);
    }

    interface Reppsitory{
        Observable<BaseJson<Integer>> doSubscribe(String follows);
        void handleSubscribe(String follows);
    }
}

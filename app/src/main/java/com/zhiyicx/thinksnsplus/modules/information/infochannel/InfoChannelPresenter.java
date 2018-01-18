package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoChannelRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_INTERVAL_TIME;
import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_MAX_COUNT;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoChannelPresenter extends AppBasePresenter<
        InfoChannelConstract.View> implements InfoChannelConstract.Presenter {

    @Inject
    InfoChannelRepository mInfoChannelRepository;

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;

    @Inject
    public InfoChannelPresenter(InfoChannelConstract
                                        .View rootView) {
        super(rootView);
    }


    @Override
    public void updateLocalInfoType(InfoTypeBean infoTypeBean) {
        mInfoTypeBeanGreenDao.updateSingleData(infoTypeBean);
    }

    @Override
    public void doSubscribe(String follows) {
        Subscription subscribe = mInfoChannelRepository.doSubscribe(follows)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void handleSubscribe(String follows) {
        mInfoChannelRepository.handleSubscribe(follows);
    }
}

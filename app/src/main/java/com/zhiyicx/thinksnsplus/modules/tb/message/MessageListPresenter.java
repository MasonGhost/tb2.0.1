package com.zhiyicx.thinksnsplus.modules.tb.message;

import com.google.gson.Gson;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.contract.ContractListContract;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lx on 2018/3/24.
 */

public class MessageListPresenter extends AppBasePresenter<MessageListContract.View> implements MessageListContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MessageListPresenter(MessageListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<TbMessageBean> data, boolean isLoadMore) {
        return false;
    }


    /**
     * 推送相关
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST)
    private void onJpushMessageRecieved(JpushMessageBean jpushMessageBean) {
        if (jpushMessageBean.getType() == null) {
            return;
        }
        Observable.just(jpushMessageBean)
                .subscribeOn(Schedulers.io())
                .map(new Func1<JpushMessageBean, TbMessageBean>() {
                    @Override
                    public TbMessageBean call(JpushMessageBean jpushMessageBean) {
                        Gson gson = new Gson();
                        TbMessageBean tbMessageBean = null;
                        try {
                            tbMessageBean = gson.fromJson(gson.toJson(jpushMessageBean.getExtras()), TbMessageBean.class);
                            //
                        } catch (Exception ignored) {

                        }
                        return tbMessageBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<TbMessageBean>() {
                    @Override
                    protected void onSuccess(TbMessageBean data) {
                
                    }
                });


    }
}

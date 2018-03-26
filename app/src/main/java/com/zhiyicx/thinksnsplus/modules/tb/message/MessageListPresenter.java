package com.zhiyicx.thinksnsplus.modules.tb.message;

import android.util.Log;

import com.google.gson.Gson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.source.local.MessageListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.contract.ContractListContract;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository.SHARETYPEENUM.FEED;

/**
 * Created by lx on 2018/3/24.
 */

public class MessageListPresenter extends AppBasePresenter<MessageListContract.View> implements MessageListContract.Presenter {

    @Inject
    MessageListBeanGreenDaoImpl mMessageListBeanGreenDao;

    /**
     * 是否有未读消息
     */
    private boolean mHasUnreadMessage;

    @Inject
    public MessageListPresenter(MessageListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = Observable.just(mMessageListBeanGreenDao.getMultiDataFromCache())
                .observeOn(Schedulers.io())
                .map(tbMessageBeans -> {
                            for (TbMessageBean tbMessageBean : tbMessageBeans) {
                                if (tbMessageBean.getMIsRead()) {
                                    mHasUnreadMessage = true;
                                    break;
                                } else {
                                    mHasUnreadMessage = false;
                                }
                            }
                            return tbMessageBeans;
                        }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<TbMessageBean>>() {
                    @Override
                    protected void onSuccess(List<TbMessageBean> data) {
                        mRootView.onCacheResponseSuccess(data, false);
                        handleHomeBottomMessageTip();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<TbMessageBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void updateMessageReadStaus(TbMessageBean tbMessageBean) {
        mMessageListBeanGreenDao.insertOrReplace(tbMessageBean);
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
                .map(jpushMessageBean1 -> {
                    Gson gson = new Gson();
                    TbMessageBean tbMessageBean = null;
                    try {
                        tbMessageBean = gson.fromJson(jpushMessageBean1.getExtras(), TbMessageBean.class);
                        tbMessageBean.setMIsRead(false);
                        tbMessageBean.setUser_id(TbMessageBean.FEED.equals(tbMessageBean.getChannel()) ? tbMessageBean.getFeed().getUser_id() : tbMessageBean
                                .getNews().getUser_id());
                        tbMessageBean.setMLoginUserId(AppApplication.getMyUserIdWithdefault());
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    if (tbMessageBean != null) {
                        mMessageListBeanGreenDao.insertOrReplace(tbMessageBean);
                        int positon = -1;

                        for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                            if (tbMessageBean.getUser_id().equals(mRootView.getListDatas().get(i).getUser_id())) {
                                positon = i;
                                break;
                            }
                        }

                        if (positon != -1) {
                            // 列表已经有数据了
                            mRootView.getListDatas().set(positon, tbMessageBean);
                        } else {
                            //列表没有数据
                            for (int i1 = 0; i1 < mRootView.getListDatas().size(); i1++) {
                                if (mRootView.getListDatas().get(i1).getMIsPinned()) {
                                    continue;
                                } else {
                                    mRootView.getListDatas().add(i1, tbMessageBean);
                                }
                            }
                        }

                    }

                    return tbMessageBean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<TbMessageBean>() {
                    @Override
                    protected void onSuccess(TbMessageBean data) {
                        if (data == null) {
                            return;
                        }
                        mRootView.refreshData();
                        handleHomeBottomMessageTip();
                    }
                });


    }

    private void handleHomeBottomMessageTip() {
        EventBus.getDefault().post(mHasUnreadMessage, EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE);
    }
}

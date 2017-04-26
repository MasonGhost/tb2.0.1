package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.support.annotation.NonNull;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationPresenter extends BasePresenter<SystemConversationContract.Repository, SystemConversationContract.View> implements SystemConversationContract.Presenter {
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    @Inject
    public SystemConversationPresenter(SystemConversationContract.Repository repository, SystemConversationContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void sendTextMessage(String text) {
        final ChatItemBean chatItemBean = new ChatItemBean();
        chatItemBean.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id())));
        final SystemConversationBean systemConversationBean = new SystemConversationBean();
        systemConversationBean.setUser_id(Long.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()));
        systemConversationBean.setContent(text);
        systemConversationBean.setCreated_at(TimeUtils.millis2String(System.currentTimeMillis()));
        systemConversationBean.setType(ApiConfig.SYSTEM_CONVERSATIONS_TYPE_FEEDBACK);
        systemConversationBean.setSystem_mark(Long.valueOf((AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis())));
        final Message message = new Message();
        message.setId(systemConversationBean.get_id().intValue());
        message.setCreate_time(System.currentTimeMillis());
        message.setTxt(systemConversationBean.getContent());
        message.setSend_status(MessageStatus.SEND_SUCCESS);
        chatItemBean.setLastMessage(message);
        mRootView.updateSendText(chatItemBean);
        mSystemConversationBeanGreenDao.insertOrReplace(systemConversationBean);
        mRepository.systemFeedback(text)
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        systemConversationBean.setId(((Double) data).longValue());
                        mSystemConversationBeanGreenDao.insertOrReplace(systemConversationBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        feedbackFail();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        feedbackFail();
                    }

                    private void feedbackFail() {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                        chatItemBean.getLastMessage().setSend_status(MessageStatus.SEND_FAIL);
                    }
                });
    }

    /**
     * 获取网络数据
     *
     * @param maxId      当前获取到数据的最大 id
     */
    @Override
    public void requestNetData(Long maxId) {
        Subscription systemconversationsSub = mRepository.getSystemConversations(maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .map(new Func1<BaseJson<List<SystemConversationBean>>, BaseJson<List<ChatItemBean>>>() {
                    @Override
                    public BaseJson<List<ChatItemBean>> call(BaseJson<List<SystemConversationBean>> listBaseJson) {
                        BaseJson<List<ChatItemBean>> chatData = new BaseJson<List<ChatItemBean>>();
                        chatData.setCode(listBaseJson.getCode());
                        chatData.setMessage(listBaseJson.getMessage());
                        chatData.setStatus(listBaseJson.isStatus());
                        if (listBaseJson.isStatus()) {
                            chatData.setData(packageChatItemBean(listBaseJson.getData()));
                        }
                        return chatData;
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<ChatItemBean>>() {
                    @Override
                    protected void onSuccess(List<ChatItemBean> data) {
                        mRootView.updateNetData(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(systemconversationsSub);
    }

    /**
     * 获取本地数据
     *
     * @param max_Id
     * @return
     */
    @Override
    public void requestCacheData(long max_Id) {
        Subscription cacheSub = Observable.just(mRepository.requestCacheData(max_Id))
                .map(new Func1<List<SystemConversationBean>, List<ChatItemBean>>() {
                    @Override
                    public List<ChatItemBean> call(List<SystemConversationBean> systemConversationBeen) {
                        return packageChatItemBean(systemConversationBeen);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new Action1<List<ChatItemBean>>() {
                    @Override
                    public void call(List<ChatItemBean> chatItemBeen) {
                        mRootView.updateCacheData(chatItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscrebe(cacheSub);
    }

    @NonNull
    private List<ChatItemBean> packageChatItemBean(List<SystemConversationBean> systemConversationBeen) {
        List<ChatItemBean> datas = new ArrayList<>();
        for (SystemConversationBean systemConversationBean : systemConversationBeen) {
            ChatItemBean chatItemBean = new ChatItemBean();
            Message message = new Message();
            message.setId(systemConversationBean.get_id().intValue());
            message.setTxt(systemConversationBean.getContent());
            message.setCreate_time(TimeUtils.string2MillisDefaultLocal(systemConversationBean.getCreated_at()));
            message.setSend_status(systemConversationBean.getId() == null ? MessageStatus.SEND_FAIL : MessageStatus.SEND_SUCCESS);
            message.setUid(systemConversationBean.getUser_id() == null ? 0 : systemConversationBean.getUser_id().intValue());
            chatItemBean.setLastMessage(message);
            if (systemConversationBean.getUserInfo() == null) {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setName(mContext.getString(R.string.ts_helper));
                chatItemBean.setUserInfo(userInfoBean);
            } else {
                chatItemBean.setUserInfo(systemConversationBean.getUserInfo());
            }
            datas.add(chatItemBean);
        }
        return datas;
    }
}

package com.zhiyicx.thinksnsplus.modules.home.message;

import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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

import static com.zhiyicx.baseproject.config.ApiConfig.FLUSHMESSAGES_KEY_NOTICES;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessagePresenter extends BasePresenter<MessageContract.Repository, MessageContract.View> implements MessageContract.Presenter {
    public static final int DEFAULT_TS_HLEPER_CONVERSATION_ID = -100; // TS 助手默认的会话 id
    private static final int MAX_USER_NUMS_COMMENT = 2;
    private static final int MAX_USER_NUMS_DIGG = 3;

    @Inject
    ChatContract.Repository mChatRepository;

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    CommentedBeanGreenDaoImpl mCommentedBeanGreenDao;

    @Inject
    DigedBeanGreenDaoImpl mDigedBeanGreenDao;
    @Inject
    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    private MessageItemBean mItemBeanComment; // 评论的
    private MessageItemBean mItemBeanDigg;    // 点赞的
    private MessageItemBean mItemBeanNotices; // ts 助手

    @Inject
    public MessagePresenter(MessageContract.Repository repository, MessageContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (AppApplication.getmCurrentLoginAuth() == null)
            return;
        mRepository.getConversationList(AppApplication.getmCurrentLoginAuth().getUser_id())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<MessageItemBean>>() {
                    @Override
                    protected void onSuccess(final List<MessageItemBean> data) {
                        if (mItemBeanNotices != null && mItemBeanNotices.getConversation().getCid() != 0) {
                            data.add(mItemBeanNotices);
                        }
                        mRootView.onNetResponseSuccess(data, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }
                });
    }

    /**
     * 没有加载更多，一次全部取出
     *
     * @param maxId
     * @param isLoadMore 加载状态
     * @return
     */
    @Override
    public List<MessageItemBean> requestCacheData(Long maxId, boolean isLoadMore) {
        if (mAuthRepository.getAuthBean() == null) {
            return new ArrayList<>();
        }
        initHeaderItemData();
        handleFlushMessageForItem(mFlushMessageBeanGreenDao.getMultiDataFromCache()); // 处理本地消息
        mRootView.updateLikeItemData(mItemBeanDigg);
        List<MessageItemBean> cacheData = mChatRepository.getConversionListData(mAuthRepository.getAuthBean().getUser_id());
        if (!isLoadMore && mItemBeanNotices != null && mItemBeanNotices.getConversation().getCid() != 0) {
            cacheData.add(mItemBeanNotices);
        }
        return cacheData;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return mChatRepository.insertOrUpdateMessageItemBean(data);
    }

    /**
     * 配置 TS 助手；如果本地没有 ts 助手，说明是第一次使用，添加一个 ts 助手
     */
    @Override
    public void configTSHelper() {
        SystemConversationBean tsHelper;
        tsHelper = mSystemConversationBeanGreenDao.getSystemConversationById(SystemConversationBean.DEFAULT_TSHELPER_SYSTEMCONVERSATION_ID);
        System.out.println("tsHelper = " + tsHelper);
        if (tsHelper == null) {
            tsHelper = new SystemConversationBean();
            tsHelper.setId(SystemConversationBean.DEFAULT_TSHELPER_SYSTEMCONVERSATION_ID);
            tsHelper.setType(ApiConfig.SYSTEM_CONVERSATIONS_TYPE_SYSTEM);
            tsHelper.setContent(mContext.getString(R.string.ts_helper_default_tip));
            tsHelper.setUser_id(0L);
            tsHelper.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
            mSystemConversationBeanGreenDao.insertOrReplace(tsHelper);
            FlushMessages noticeFlushMessage = new FlushMessages();
            noticeFlushMessage.setTime(tsHelper.getCreated_at());
            noticeFlushMessage.setCount(1);
            noticeFlushMessage.setKey(ApiConfig.FLUSHMESSAGES_KEY_NOTICES);
            mFlushMessageBeanGreenDao.insertOrReplace(noticeFlushMessage);
        }
    }

    @Override
    public MessageItemBean updateCommnetItemData() {
        return mItemBeanComment;
    }

    @Override
    public MessageItemBean updateLikeItemData() {
        return mItemBeanDigg;
    }

    @Override
    public MessageItemBean updateNoticesItemData() {
        return mItemBeanNotices;
    }

    /**
     * 刷新是否显示底部红点
     * 刷新当条item 的未读数
     */
    @Override
    public void refreshConversationReadMessage() {
        Subscription represhSu = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, List<MessageItemBean>>() {
                    @Override
                    public List<MessageItemBean> call(String s) {
                        int size = mRootView.getListDatas().size();
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getListDatas().get(i).getConversation().getCid() != DEFAULT_TS_HLEPER_CONVERSATION_ID) { // 聊天消息
                                Message message = MessageDao.getInstance(mContext).getLastMessageByCid(mRootView.getListDatas().get(i).getConversation().getCid());
                                mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                                mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                                mRootView.getListDatas().get(i).setUnReadMessageNums(MessageDao.getInstance(mContext).getUnReadMessageCount(mRootView.getListDatas().get(i).getConversation().getCid()));
                            } else { // Ts 助手
                                SystemConversationBean systemConversationBean = mSystemConversationBeanGreenDao.getLastData();
                                if (systemConversationBean != null) {
                                    mRootView.getListDatas().get(i).getConversation().getLast_message().setTxt(systemConversationBean.getContent());
                                    mRootView.getListDatas().get(i).getConversation().getLast_message().setCreate_time(TimeUtils.utc2LocalLong(systemConversationBean.getCreated_at()));
                                    mRootView.getListDatas().get(i).getConversation().setLast_message_time(TimeUtils.utc2LocalLong(systemConversationBean.getCreated_at()));
                                }
                            }
                        }

                        return mRootView.getListDatas();
                    }
                })
                .subscribe(new Action1<List<MessageItemBean>>() {
                    @Override
                    public void call(List<MessageItemBean> data) {
//                        mRootView.onCacheResponseSuccess(data, false);
                        mRootView.refreshData();
                        checkBottomMessageTip();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        addSubscrebe(represhSu);
    }

    @Override
    public void deletConversation(MessageItemBean messageItemBean) {
        ConversationDao.getInstance(mContext).delConversation(messageItemBean.getConversation().getCid(), messageItemBean.getConversation().getType());
    }

    @Override
    public void getSingleConversation(int cid) {
        mRepository.getSingleConversation(cid)
                .subscribe(new BaseSubscribe<MessageItemBean>() {
                    @Override
                    protected void onSuccess(MessageItemBean data) {
                        if (mRootView.getListDatas().size() == 0) {
                            mRootView.getListDatas().add(data);
                        } else {
                            mRootView.getListDatas().add(0, data);// 置顶新消息
                        }
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void readMessageByKey(String key) {
        mFlushMessageBeanGreenDao.readMessageByKey(key);
    }

    /*******************************************
     * IM 相关
     *********************************************/

    /**
     * 收到聊天消息
     *
     * @param message 聊天类容
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        int size = mRootView.getListDatas().size();
        boolean isHasConversion = false; // 对话是否存在
        for (int i = 0; i < size; i++) {
            if (mRootView.getListDatas().get(i).getConversation().getCid() == message.getCid()) {
                mRootView.getListDatas().get(i).setUnReadMessageNums(mRootView.getListDatas().get(i).getUnReadMessageNums() + 1);
                mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                mRootView.getListDatas().add(0, mRootView.getListDatas().get(i)); // 加到第一个
                mRootView.getListDatas().remove(i + 1);
                mRootView.refreshData(); // 加上 header 的位置
                isHasConversion = true;
                break;
            }
        }
        if (!isHasConversion) { // 不存在本地对话，直接服务器获取
            getSingleConversation(message.getCid());
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED)
    private void onMessageACKReceived(Message message) {
        if (!(ActivityHandler.getInstance().currentActivity() instanceof HomeActivity)) {
            return;
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_AUTHSUCESSED)
    private void onAuthSuccessed(AuthData authData) {
//        mRootView.showSnackSuccessMessage("IM 聊天加载成功");
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONNECTED)
    private void onConnected() {
        mRootView.hideStickyMessage();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONDISCONNECT)
    private void onDisconnect(int code, String reason) {
        mRootView.showStickyMessage(reason);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONERROR)
    private void onError(Exception error) {
        mRootView.showMessage(error.toString());
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT)
    private void onMessaeTimeout(Message message) {

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONVERSATIONCRATED)
    private void onConversationCreated(MessageItemBean messageItemBean) {
        mRootView.getListDatas().add(0, messageItemBean);
        mRootView.refreshData();
    }

    /**
     * 推送相关
     *
     * @param jpushMessageBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST)
    private void onJpushMessageRecieved(JpushMessageBean jpushMessageBean) {

        switch (jpushMessageBean.getType()) {
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM: // 推送携带的消息  {"seq":36,"msg_type":0,"cid":1,"mid":338248648800337924,"type":"im","uid":20} IM 消息通过IM接口 同步，故不需要对 推送消息做处理
                handleIMPush(jpushMessageBean);
                break;
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_FEED:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_CHANNEL:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_MUSIC:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_NEWS:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_USER:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_SYSTEM:
            default:
                switch (jpushMessageBean.getAction()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_COMMENT:
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_DIGG:
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_FOLLOW:
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_NOTICE:
                        mSystemRepository.getSystemConversations(System.currentTimeMillis(), TSListFragment.DEFAULT_PAGE_SIZE)
                                .subscribe(new BaseSubscribe<List<SystemConversationBean>>() {
                                    @Override
                                    protected void onSuccess(List<SystemConversationBean> data) {
                                        // 服务器同步未读评论和点赞消息
                                        handleFlushMessage();
                                    }

                                    @Override
                                    protected void onFailure(String message, int code) {

                                    }

                                    @Override
                                    protected void onException(Throwable throwable) {

                                    }
                                });
                        break;
                    default:
                        // 服务器同步未读评论和点赞消息
                        handleFlushMessage();
                        break;
                }
                break;

        }
    }

    /**
     * 处理聊天推送
     *
     * @param jpushMessageBean
     */
    private void handleIMPush(JpushMessageBean jpushMessageBean) {
        String extras = jpushMessageBean.getExtras();
        try {
            JSONObject jsonObject = new JSONObject(extras);
            Message message = new Message();
            message.setCid(jsonObject.getInt("cid"));
            message.setSeq(jsonObject.getInt("seq"));
            ZBIMClient.getInstance().syncAsc(message.getCid(), message.getSeq() - 1, message.getSeq() + 1, (int) System.currentTimeMillis());// 获取推送的信息
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理 获取用户收到的最新消息
     *
     * @return
     */
    @Override
    public void handleFlushMessage() {
        Long last_request_time = SharePreferenceUtils.getLong(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME);
        if (last_request_time == 0) {
            last_request_time = System.currentTimeMillis() / 1000 - ConstantConfig.HOUR / 1000;
        }
        last_request_time++;//  由于请求接口数据时间是以秒级时间戳 建议调用传入时间间隔1秒以上 以防止数据重复
        mUserInfoRepository.getMyFlushMessage(last_request_time, "")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.showTopRightLoading();

                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.closeTopRightLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<FlushMessages>>() {
                    @Override
                    protected void onSuccess(List<FlushMessages> data) {
                        SharePreferenceUtils.saveLong(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME, System.currentTimeMillis() / 1000);
                        FlushMessages commentFlushMessage = null;
                        FlushMessages diggFlushMessage = null;
                        FlushMessages followFlushMessage = null;
                        FlushMessages noticeFlushMessage = null;

                        List<FlushMessages> flushMessagesList = mFlushMessageBeanGreenDao.getMultiDataFromCache();
                        if (!flushMessagesList.isEmpty()) {
                            for (FlushMessages flushMessages : flushMessagesList) {
                                switch (flushMessages.getKey()) {
                                    case ApiConfig.FLUSHMESSAGES_KEY_COMMENTS:
                                        commentFlushMessage = flushMessages;
                                        break;
                                    case ApiConfig.FLUSHMESSAGES_KEY_DIGGS:
                                        diggFlushMessage = flushMessages;
                                        break;
                                    case ApiConfig.FLUSHMESSAGES_KEY_FOLLOWS:
                                        followFlushMessage = flushMessages;
                                        break;
                                    case FLUSHMESSAGES_KEY_NOTICES:
                                        noticeFlushMessage = flushMessages;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            for (FlushMessages flushMessage : data) {
                                if (flushMessage.getCount() == 0) {
                                    continue;
                                }
                                switch (flushMessage.getKey()) {
                                    case ApiConfig.FLUSHMESSAGES_KEY_COMMENTS:
                                        MessagePresenter.this.handleFlushMessage(flushMessage, commentFlushMessage);
                                        break;
                                    case ApiConfig.FLUSHMESSAGES_KEY_DIGGS:
                                        MessagePresenter.this.handleFlushMessage(flushMessage, diggFlushMessage);
                                        break;
                                    case ApiConfig.FLUSHMESSAGES_KEY_FOLLOWS:
                                        MessagePresenter.this.handleFlushMessage(flushMessage, followFlushMessage);
                                        break;
                                    case FLUSHMESSAGES_KEY_NOTICES:
                                        MessagePresenter.this.handleFlushMessage(flushMessage, noticeFlushMessage);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            handleFlushMessageForItem(flushMessagesList);
                        } else {
                            handleFlushMessageForItem(data);
                        }
                        mRootView.updateLikeItemData(mItemBeanDigg);
                        // 更新我的消息提示
                        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE);

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });

    }

    private void handleFlushMessageForItem(List<FlushMessages> flushMessages) {
        for (FlushMessages flushMessage : flushMessages) {
            if (flushMessage.getCount() == 0) {
                continue;
            }
            mFlushMessageBeanGreenDao.insertOrReplace(flushMessage);
            switch (flushMessage.getKey()) {
                case ApiConfig.FLUSHMESSAGES_KEY_COMMENTS:
                    handleItemBean(mItemBeanComment, flushMessage);
                    break;
                case ApiConfig.FLUSHMESSAGES_KEY_DIGGS:
                    handleItemBean(mItemBeanDigg, flushMessage);
                    break;
                case ApiConfig.FLUSHMESSAGES_KEY_FOLLOWS:
                    break;
                case FLUSHMESSAGES_KEY_NOTICES:
                    handleItemBean(mItemBeanNotices, flushMessage);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 头部信息和 TS 助手数据赋值
     *
     * @param messageItemBean
     * @param flushMessage
     */
    private void handleItemBean(MessageItemBean messageItemBean, FlushMessages flushMessage) {
        String textEndTip = "";
        int max_user_nums = MAX_USER_NUMS_COMMENT;
        switch (flushMessage.getKey()) {
            case ApiConfig.FLUSHMESSAGES_KEY_COMMENTS:
                textEndTip = mContext.getString(R.string.comment_me);
                max_user_nums = MAX_USER_NUMS_COMMENT;
                CommentedBean lastCommentedBean = mCommentedBeanGreenDao.getLastData();
                if (lastCommentedBean != null && flushMessage.getMax_id() != 0 && lastCommentedBean.getId() > flushMessage.getMax_id()) {// 如果本地查看的数据 id 已经大于 新消息的 id 说明已经读取过了
                    flushMessage.setCount(0);
                }
                break;
            case ApiConfig.FLUSHMESSAGES_KEY_DIGGS:
                textEndTip = mContext.getString(R.string.like_me);
                max_user_nums = MAX_USER_NUMS_DIGG;
                DigedBean lastDiggBend = mDigedBeanGreenDao.getLastData();
                if (lastDiggBend != null && flushMessage.getMax_id() != 0 && lastDiggBend.getId() > flushMessage.getMax_id()) {
                    flushMessage.setCount(0);
                }

                break;
            case FLUSHMESSAGES_KEY_NOTICES:
                SystemConversationBean systemConversationBean = mSystemConversationBeanGreenDao.getLastData();
                if (systemConversationBean == null) {
                    textEndTip = mContext.getString(R.string.ts_helper_default_tip);
                } else {
                    textEndTip = systemConversationBean.getContent();
                }

                break;
            default:
                break;
        }
        if (messageItemBean == null) {
            messageItemBean = new MessageItemBean();
            Conversation commentMessage = new Conversation();
            Message message = new Message();
            commentMessage.setLast_message(message);
            messageItemBean.setConversation(commentMessage);
        }
        messageItemBean.setUnReadMessageNums(flushMessage.getCount());
        messageItemBean.getConversation().setLast_message_time(TextUtils.isEmpty(flushMessage.getTime()) ? System.currentTimeMillis() : TimeUtils.utc2LocalLong(flushMessage.getTime()));
        messageItemBean.getConversation().getLast_message().setCreate_time(TextUtils.isEmpty(flushMessage.getTime()) ? System.currentTimeMillis() : TimeUtils.utc2LocalLong(flushMessage.getTime()));
        String text = mContext.getString(R.string.has_no_body);
        if (!TextUtils.isEmpty(flushMessage.getUids())) {
            text = "";
            String[] uids = flushMessage.getUids().split(ConstantConfig.SPLIT_SMBOL);
            List<String> ueridSet = new ArrayList<>();
            for (int length = 0; length < uids.length; length++) {
                if (!TextUtils.isEmpty(uids[length]) && !ueridSet.contains(uids[length])) {// 用户 信息去重,去空
                    ueridSet.add(uids[length]);
                }
            }
            int i = 0;
            for (String s : ueridSet) {
                if (i++ < max_user_nums) {
                    try {
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(s));
                        text += userInfoBean.getName() + "、";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            switch (flushMessage.getKey()) { // 超过限定的人数才显示 “等人"
                case ApiConfig.FLUSHMESSAGES_KEY_COMMENTS:
                    if (ueridSet.size() > MAX_USER_NUMS_COMMENT) {
                        text += mContext.getString(R.string.comment_digg_much_hint);
                    }
                    break;
                case ApiConfig.FLUSHMESSAGES_KEY_DIGGS:
                    if (ueridSet.size() > MAX_USER_NUMS_DIGG) {
                        text += mContext.getString(R.string.comment_digg_much_hint);
                    }
                    break;
                default:
                    break;

            }
        }
        if (text.endsWith("、")) {
            text = text.substring(0, text.length() - 1);
        }
        if (flushMessage.getKey().equals(FLUSHMESSAGES_KEY_NOTICES)) {
            messageItemBean.getConversation().getLast_message().setTxt(
                    textEndTip);
            mRootView.updateTSHelper(mItemBeanNotices);
        } else {
            messageItemBean.getConversation().getLast_message().setTxt(text
                    + textEndTip);
        }
        checkBottomMessageTip();
    }

    /**
     * @param commentFlushMessage from net
     * @param flushMessage        from local
     */
    private void handleFlushMessage(FlushMessages commentFlushMessage, FlushMessages flushMessage) {
        if (commentFlushMessage == null) {
            return;
        }
        if (flushMessage == null) {
            mFlushMessageBeanGreenDao.insertOrReplace(commentFlushMessage);
            return;
        }
        flushMessage.setCount(commentFlushMessage.getCount() + flushMessage.getCount());
        flushMessage.setTime(commentFlushMessage.getTime());
        if (commentFlushMessage.getCount() >= MAX_USER_NUMS_COMMENT) {
            flushMessage.setUids(commentFlushMessage.getUids());
        } else {
            String uids = ConvertUtils.removeSymbolStartWith(commentFlushMessage.getUids() + ConstantConfig.SPLIT_SMBOL + flushMessage.getUids(), ConstantConfig.SPLIT_SMBOL);
            uids = ConvertUtils.removeSymbolEndWith(uids, ConstantConfig.SPLIT_SMBOL);
            flushMessage.setUids(uids);
        }
    }

    /**
     * 初始化 header 数据
     */
    private void initHeaderItemData() {
        mItemBeanComment = new MessageItemBean();
        Conversation commentMessage = new Conversation();
        Message message = new Message();
        commentMessage.setLast_message(message);
        mItemBeanComment.setConversation(commentMessage);
        mItemBeanComment.getConversation().getLast_message().setTxt(mContext.getString(R.string.has_no_body)
                + mContext.getString(R.string.comment_me));

        mItemBeanDigg = new MessageItemBean();
        Conversation diggConveration = new Conversation();
        Message diggmessage = new Message();
        diggConveration.setLast_message(diggmessage);
        mItemBeanDigg.setConversation(diggConveration);
        mItemBeanDigg.getConversation().getLast_message().setTxt(mContext.getString(R.string.has_no_body)
                + mContext.getString(R.string.like_me));

        mItemBeanNotices = new MessageItemBean();
        Conversation noticeConveration = new Conversation();
        noticeConveration.setCid(DEFAULT_TS_HLEPER_CONVERSATION_ID);
        Message noticemessage = new Message();
        noticeConveration.setLast_message(noticemessage);
        mItemBeanNotices.setConversation(noticeConveration);
        mItemBeanNotices.getConversation().getLast_message().setTxt(mContext.getString(R.string.ts_helper_default_tip));
        UserInfoBean tsHelperUserInfo = new UserInfoBean();
        tsHelperUserInfo.setName(mContext.getString(R.string.ts_helper));
        mItemBeanNotices.setUserInfo(tsHelperUserInfo);

    }


    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        // 是否显示底部红点
        boolean isShowMessgeTip;
        if (mItemBeanNotices != null && mItemBeanDigg != null && mItemBeanComment != null
                && mItemBeanNotices.getUnReadMessageNums() == 0
                && mItemBeanDigg.getUnReadMessageNums() == 0
                && mItemBeanComment.getUnReadMessageNums() == 0) {
            isShowMessgeTip = false;
        } else {
            isShowMessgeTip = true;
        }
        if (!isShowMessgeTip) {
            for (MessageItemBean messageItemBean : mRootView.getListDatas()) {
                if (messageItemBean.getUnReadMessageNums() > 0) {
                    isShowMessgeTip = true;
                    break;
                } else {
                    isShowMessgeTip = false;
                }
            }
        }

        EventBus.getDefault().post(isShowMessgeTip, EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE);
    }

}
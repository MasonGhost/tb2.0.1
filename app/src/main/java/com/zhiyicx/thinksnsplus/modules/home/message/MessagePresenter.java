package com.zhiyicx.thinksnsplus.modules.home.message;

import android.text.TextUtils;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
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
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_COMMENTS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_DIGGS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_REPLY_COMMENTS;
import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessagePresenter extends AppBasePresenter<MessageContract.Repository, MessageContract.View> implements MessageContract.Presenter {
    private static final int MAX_USER_NUMS_COMMENT = 2;
    private static final int MAX_USER_NUMS_DIGG = 3;
    public static final int DEFAULT_MAX_REQUEST_UNREAD_NUM = 100;

    @Inject
    ChatContract.Repository mChatRepository;

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

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
    private MessageItemBean mItemBeanReview;    // 评论置顶的

    private int mUnreadNotificationTotalNums; // 未读消息总数

    List<TSPNotificationBean> mCommentsNoti = new ArrayList<>();
    List<TSPNotificationBean> mDiggNoti = new ArrayList<>();
    List<TSPNotificationBean> mReviewNoti = new ArrayList<>();

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
        creatTsHelperConversation();
    }

    /**
     * 创建 ts 助手对话
     */
    public void creatTsHelperConversation() {
        List<Observable<Conversation>> datas = new ArrayList<>();
        final List<SystemConfigBean.ImHelperBean> tsHlepers = mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
        // 新版 ts 助手
        for (final SystemConfigBean.ImHelperBean imHelperBean : tsHlepers) {
            if (imHelperBean.isDelete()) {
                continue;
            }
            final String uidsstr = AppApplication.getMyUserIdWithdefault() + "," + imHelperBean.getUid();
            datas.add(mChatRepository.createConveration(ChatType.CHAT_TYPE_PRIVATE, "", "", uidsstr));
        }
        if (datas.isEmpty()) {
            getCoversationList();
        } else {
            Observable.zip(datas, (FuncN<Object>) args -> {
                for (int i = 0; i < args.length; i++) {  // 为 ts 助手添加提示语
                    Conversation data = ((Conversation) args[i]);
                    // 写入 ts helper 默认提示语句
                    long currentTime = System.currentTimeMillis();
                    Message message = new Message();
                    message.setId((int) currentTime);
                    message.setType(MessageType.MESSAGE_TYPE_TEXT);
                    message.setTxt(mContext.getString(R.string.ts_helper_default_tip));
                    message.setSend_status(MessageStatus.SEND_SUCCESS);
                    message.setIs_read(false);
                    message.setUid(Integer.parseInt(tsHlepers.get(i).getUid()));
                    message.setCid(data.getCid());
                    message.setCreate_time(currentTime);
//                    public static final long TIME_DEFAULT_ADD = 1451577600000L; //  消息的MID，`(mid >> 23) + 1451577600000` 为毫秒时间戳
                    message.setMid((currentTime - TIME_DEFAULT_ADD) << 23);
                    MessageDao.getInstance(mContext).insertOrUpdateMessage(message);
                }
                return args;
            }).subscribe(new BaseSubscribeForV2<Object>() {
                @Override
                protected void onSuccess(Object data) {
                    getCoversationList();
                }
            });
        }

    }

    /**
     * 获取对话列表
     */
    private void getCoversationList() {
        mRepository.getConversationList((int) AppApplication.getmCurrentLoginAuth().getUser_id())
                .doAfterTerminate(() -> mRootView.hideLoading())
                .subscribe(new BaseSubscribeForV2<List<MessageItemBean>>() {
                    @Override
                    protected void onSuccess(final List<MessageItemBean> data) {
                        mRootView.onNetResponseSuccess(data, false);
                        refreshConversationReadMessage();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
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
        // 处理本地通知数据

        mRootView.updateLikeItemData(mItemBeanDigg);
        List<MessageItemBean> cacheData = mChatRepository.getConversionListData(mAuthRepository.getAuthBean().getUser_id());
        return cacheData;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return mChatRepository.insertOrUpdateMessageItemBean(data);
    }

    /**
     * 检测 ts helper 是否是当前用户
     */
    @Override
    public String checkTShelper(long user_id) {
        return mSystemRepository.checkTShelper(user_id);
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
    public MessageItemBean updateReviewItemData() {
        return mItemBeanReview;
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
                .map(s -> {
                    int size = mRootView.getListDatas().size();
                    for (int i = 0; i < size; i++) {
                        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(mRootView.getListDatas().get(i).getConversation().getCid());
                        mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                        mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                        mRootView.getListDatas().get(i).setUnReadMessageNums(MessageDao.getInstance(mContext).getUnReadMessageCount(mRootView.getListDatas().get(i).getConversation().getCid()));
                    }

                    return mRootView.getListDatas();
                })
                .subscribe(data -> {
                    mRootView.refreshData();
                    checkBottomMessageTip();
                }, throwable -> throwable.printStackTrace());
        addSubscrebe(represhSu);
    }

    /**
     * 删除对话信息
     *
     * @param position
     */
    @Override
    public void deletConversation(int position) {
        final MessageItemBean messageItemBean = mRootView.getListDatas().get(position);
        // ts 助手标记为已删除
        Subscription subscribe = Observable.empty()
                .observeOn(Schedulers.newThread())
                .subscribe(new rx.Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        String[] uidsTmp = messageItemBean.getConversation().getUsids().split(",");
                        long toChatUser_id = Long.valueOf((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id() + "") ? uidsTmp[1] : uidsTmp[0]));
                        SystemRepository.updateTsHelperDeletStatus(mContext, toChatUser_id, true);
                        MessageDao.getInstance(mContext).delEverMessageByCid(messageItemBean.getConversation().getCid());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

        // 删除对话信息
        ConversationDao.getInstance(mContext).delConversation(messageItemBean.getConversation().getCid(), messageItemBean.getConversation().getType());
        mRootView.getListDatas().remove(position);
        checkBottomMessageTip();
        addSubscrebe(subscribe);
    }

    @Override
    public void getSingleConversation(int cid) {
        mRepository.getSingleConversation(cid)
                .subscribe(new BaseSubscribeForV2<MessageItemBean>() {
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
        String notificationIds = "";
        switch (key) {
            case NOTIFICATION_KEY_FEED_COMMENTS:
            case NOTIFICATION_KEY_FEED_REPLY_COMMENTS:
                notificationIds = getNotificationIds(mCommentsNoti, notificationIds);
                break;
            case NOTIFICATION_KEY_FEED_DIGGS:
                notificationIds = getNotificationIds(mDiggNoti, notificationIds);
                break;
            case NOTIFICATION_KEY_FEED_PINNED_COMMENT:
                notificationIds = getNotificationIds(mReviewNoti, notificationIds);
                break;
            default:
        }

        mRepository.makeNotificationReaded(notificationIds)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        LogUtils.d("makeNotificationReaded::" + "onSuccess");
                    }
                });
    }

    private String getNotificationIds(List<TSPNotificationBean> datas, String notificationIds) {
        for (TSPNotificationBean tspNotificationBean : datas) {
            if (TextUtils.isEmpty(tspNotificationBean.getRead_at())) { //代表未读
                notificationIds += tspNotificationBean.getId() + ",";
            }
        }
        return notificationIds;
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
     * 未读数获取到
     *
     * @param unreadNum unread  notificaiton nums
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UNREAD_NOTIFICATION_LIMIT)
    private void onCheckUnreadNotifyRecieved(String unreadNum) {
        try {
            mUnreadNotificationTotalNums = Integer.parseInt(unreadNum);
        } catch (Exception e) {
        }

    }

    /**
     * 推送相关
     *
     * @param jpushMessageBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST)
    private void onJpushMessageRecieved(JpushMessageBean jpushMessageBean) {
        if (jpushMessageBean.getType() == null) {
            return;
        }
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
                        // 服务器同步未读评论和点赞消息
                        handleFlushMessage();
                        break;
                    default:
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
     * 检测未读消息数
     */
    @Override
    public void checkUnreadNotification() {
        mRepository.ckeckUnreadNotification()
                .subscribe(new BaseSubscribeForV2<Void>() {
                    @Override
                    protected void onSuccess(Void data) {
                        LogUtils.i("test notification", data);
                    }
                });
    }

    /**
     * 处理 获取用户收到的最新消息
     *
     * @return
     */
    @Override
    public void handleFlushMessage() {
        Long last_request_time = SharePreferenceUtils.getLong(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME);
        if (last_request_time != 0) { // 当等于 0 时 ，服务器返回历史的用户信息
            last_request_time++;//  由于请求接口数据时间是以秒级时间戳 建议调用传入时间间隔1秒以上 以防止数据重复
        }
        mRepository.getNotificationList(null, ApiConfig.NOTIFICATION_TYPE_ALL, mUnreadNotificationTotalNums == 0 ? DEFAULT_MAX_REQUEST_UNREAD_NUM : mUnreadNotificationTotalNums, 0)
                .subscribe(new BaseSubscribeForV2<List<TSPNotificationBean>>() {
                    @Override
                    protected void onSuccess(List<TSPNotificationBean> data) {
                        if (data.isEmpty()) {
                            return;
                        }
                        mCommentsNoti.clear();
                        mDiggNoti.clear();
                        mReviewNoti.clear();
                        for (TSPNotificationBean tspNotificationBean : data) {
                            switch (tspNotificationBean.getData().getChannel()) {
                                case NOTIFICATION_KEY_FEED_COMMENTS:
                                case NOTIFICATION_KEY_FEED_REPLY_COMMENTS:
                                    mCommentsNoti.add(tspNotificationBean);
                                    break;
                                case NOTIFICATION_KEY_FEED_DIGGS:
                                    mDiggNoti.add(tspNotificationBean);
                                    break;
                                case NOTIFICATION_KEY_FEED_PINNED_COMMENT:
                                    mReviewNoti.add(tspNotificationBean);
                                    break;
                                default:
                            }
                        }
                        /**
                         * 设置未读数
                         */
                        mItemBeanComment.setUnReadMessageNums(getUnreadNums(mCommentsNoti));
                        mItemBeanDigg.setUnReadMessageNums(getUnreadNums(mDiggNoti));
                        mItemBeanReview.setUnReadMessageNums(getUnreadNums(mReviewNoti));

                        /**
                         * 设置时间
                         */
                        mItemBeanComment.getConversation().setLast_message_time(mCommentsNoti.isEmpty() ? System.currentTimeMillis() : TimeUtils.utc2LocalLong(mCommentsNoti.get(0).getCreated_at()));
                        mItemBeanDigg.getConversation().setLast_message_time(mDiggNoti.isEmpty() ? System.currentTimeMillis() : TimeUtils.utc2LocalLong(mDiggNoti.get(0).getCreated_at()));
                        mItemBeanReview.getConversation().setLast_message_time(mReviewNoti.isEmpty() ? System.currentTimeMillis() : TimeUtils.utc2LocalLong(mReviewNoti.get(0).getCreated_at()));

                        /**
                         * 设置提示内容
                         * mContext.getString(R.string.has_no_body)
                         + mContext.getString(R.string.comment_me)
                         */
                        String commentTip = getItemTipStr(mCommentsNoti, MAX_USER_NUMS_COMMENT);
                        if (!TextUtils.isEmpty(commentTip)) {
                            commentTip += mContext.getString(R.string.comment_me);
                        } else {
                            commentTip = mContext.getString(R.string.has_no_body)
                                    + mContext.getString(R.string.comment_me);
                        }
                        mItemBeanComment.getConversation().getLast_message().setTxt(
                                commentTip);

                        String diggTip = getItemTipStr(mDiggNoti, MAX_USER_NUMS_DIGG);
                        if (!TextUtils.isEmpty(diggTip)) {
                            diggTip += mContext.getString(R.string.like_me);
                        } else {
                            diggTip = mContext.getString(R.string.has_no_body)
                                    + mContext.getString(R.string.like_me);
                        }
                        mItemBeanDigg.getConversation().getLast_message().setTxt(
                                diggTip);

                        String reviewTip = getItemTipStr(mReviewNoti, MAX_USER_NUMS_COMMENT);
                        if (!TextUtils.isEmpty(reviewTip)) {
                            reviewTip += mContext.getString(R.string.recieved_review);
                        } else {
                            reviewTip = mContext.getString(R.string.has_no_body)
                                    + mContext.getString(R.string.recieved_review);
                        }
                        mItemBeanReview.getConversation().getLast_message().setTxt(
                                reviewTip);


                        mRootView.updateLikeItemData(mItemBeanDigg);
                        // 更新我的消息提示
                        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE);
                    }
                });
        mUnreadNotificationTotalNums = 0;

    }

    private int getUnreadNums(List<TSPNotificationBean> datas) {
        int nums = 0;
        for (TSPNotificationBean tspNotificationBean : datas) {
            if (tspNotificationBean.getRead_at() == null) {
                nums++;
            }
        }
        return nums;
    }

    private String getItemTipStr(List<TSPNotificationBean> commentsNoti, int max_num) {
        String tip = "";
        for (int i = 0; i < commentsNoti.size(); i++) {
            if (i < max_num) {
                try {
                    if (tip.contains(commentsNoti.get(i).getUserInfo().getName())) {
                        max_num++;
                    } else {
                        tip += commentsNoti.get(i).getUserInfo().getName() + "、";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                break;
            }
        }
        if (tip.endsWith("、")) {
            tip = tip.substring(0, tip.length() - 1);
        }
        return tip;
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

        mItemBeanReview = new MessageItemBean();
        Conversation reviewConveration = new Conversation();
        Message reviewmessage = new Message();
        reviewConveration.setLast_message(reviewmessage);
        mItemBeanReview.setConversation(reviewConveration);
        mItemBeanReview.getConversation().getLast_message().setTxt(mContext.getString(R.string.has_no_body)
                + mContext.getString(R.string.recieved_review));
    }


    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        // 是否显示底部红点
        boolean isShowMessgeTip;
        if (mItemBeanDigg != null && mItemBeanComment != null
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
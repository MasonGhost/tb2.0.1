package com.zhiyicx.thinksnsplus.modules.home.message;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.ActivityHandler;
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
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;
import com.zhiyicx.thinksnsplus.data.beans.UnreadCountBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

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
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;
import static com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository.DEFAULT_TS_HELPER_TIP_MSG_ID;

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

    /**
     * 评论的
     */
    private MessageItemBean mItemBeanComment;
    /**
     * 点赞的
     */
    private MessageItemBean mItemBeanDigg;
    /**
     * 评论置顶的
     */
    private MessageItemBean mItemBeanReview;

    /**
     * 通知的小红点
     */
    private boolean mNotificaitonRedDotIsShow;

    /**
     * 用户未读消息
     */
    private UnReadNotificaitonBean mUnReadNotificaitonBean;

    private Subscription mUnreadNotiSub;

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
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        creatTsHelperConversation();
    }

    /**
     * mNotificaitonRedDotIsShow
     * 创建 ts 助手对话
     */
    public void creatTsHelperConversation() {
        Subscription subscribe = Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(integer -> {
                    List<Observable<Conversation>> datas = new ArrayList<>();
                    final List<SystemConfigBean.ImHelperBean> tsHlepers = mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
                    // 新版 ts 助手
                    for (final SystemConfigBean.ImHelperBean imHelperBean : tsHlepers) {
                        if (imHelperBean.isDelete()) {
                            continue;
                        }
                        final String uidsstr = AppApplication.getMyUserIdWithdefault() + "," + imHelperBean.getUid();
                        datas.add(mChatRepository.createConveration(ChatType.CHAT_TYPE_PRIVATE, "", "", uidsstr).observeOn(Schedulers.io()));
                    }
                    if (datas.isEmpty()) {
                        return mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault());
                    } else {
                        return Observable.zip(datas, (FuncN<Object>) args -> {
                            // 为 ts 助手添加提示语
                            for (int i = 0; i < args.length; i++) {
                                Conversation data = ((Conversation) args[i]);
                                // 写入 ts helper 默认提示语句
                                long currentTime = System.currentTimeMillis();
                                Message message = new Message();
                                message.setId(DEFAULT_TS_HELPER_TIP_MSG_ID);
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
                        }).flatMap(o -> mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
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
        addSubscrebe(subscribe);


    }

    /**
     * 没有加载更多，一次全部取出
     *
     * @param maxId
     * @param isLoadMore 加载状态
     * @return
     */
    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (mAuthRepository.getAuthBean() == null) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
        } else {
            initHeaderItemData();
            // 处理本地通知数据
            mRootView.updateLikeItemData(mItemBeanDigg);
            creatTsHelperConversation();

        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return mChatRepository.insertOrUpdateMessageItemBean(data);
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
                .observeOn(Schedulers.io())
                .map(s -> {
                    for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(mRootView.getListDatas().get(i).getConversation()
                                .getCid());
                        if (message != null) {
                            mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                            mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                            mRootView.getListDatas().get(i).setUnReadMessageNums(MessageDao.getInstance(mContext).getUnReadMessageCount(mRootView
                                    .getListDatas().get(i).getConversation().getCid()));
                        } else {
                            mRootView.getListDatas().remove(i);
                        }
                    }
                    checkBottomMessageTip();
                    return mRootView.getListDatas();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    mRootView.refreshData();
                }, Throwable::printStackTrace);
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
                        long toChatUser_id = Long.valueOf((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id() + "") ? uidsTmp[1]
                                : uidsTmp[0]));
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
        ConversationDao.getInstance(mContext).delConversation(messageItemBean.getConversation().getCid(), messageItemBean.getConversation().getType
                ());
        mRootView.getListDatas().remove(position);
        checkBottomMessageTip();
        addSubscrebe(subscribe);
    }

    @Override
    public void getSingleConversation(int cid) {
        Subscription subscribe = mRepository.getSingleConversation(cid)
                .observeOn(Schedulers.io())
                .map(data -> {
                    if (data == null || data.getConversation() == null) {
                        return false;
                    }
                    int size = mRootView.getListDatas().size();
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getConversation().getCid() == cid) {
                            return false;
                        }
                    }
                    if (mRootView.getListDatas().size() == 0) {
                        mRootView.getListDatas().add(data);
                    } else {
                        // 置顶新消息
                        mRootView.getListDatas().add(0, data);
                    }
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean data) {
                        if (data) {
                            mRootView.refreshData();
                        }
                    }

                });
        addSubscrebe(subscribe);
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
                        LogUtils.i("addBtnAnimation notification", data);
                    }
                });
    }

    @Override
    public UnReadNotificaitonBean getUnreadNotiBean() {
        return mUnReadNotificaitonBean;
    }

    /**
     * 未读数获取到
     *
     * @param unreadNumStr unread  notificaiton nums
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UNREAD_NOTIFICATION_LIMIT)
    private void onCheckUnreadNotifyRecieved(String unreadNumStr) {
        int unreadNum = 0;
        try {
            unreadNum = Integer.parseInt(unreadNumStr);

        } catch (Exception igonred) {
        }
        mNotificaitonRedDotIsShow = unreadNum > 0;
        checkBottomMessageTip();
    }


    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_NOTIFICATION_TIP_VISABLE)
    private void updateNotificaitonReddot(boolean isHide) {
        mNotificaitonRedDotIsShow = false;
        checkBottomMessageTip();
    }

    /*******************************************
     * IM 相关
     *********************************************/

    /**
     * 收到聊天消息
     *
     * @param messageData 聊天类容
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message messageData) {
        Subscription subscribe = Observable.just(messageData)
                .observeOn(Schedulers.io())
                .map(message -> {
                    int size = mRootView.getListDatas().size();
                    // 对话是否存在
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getConversation().getCid() == message.getCid()) {
                            mRootView.getListDatas().get(i).setUnReadMessageNums(mRootView.getListDatas().get(i).getUnReadMessageNums() + 1);
                            mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                            mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                            // 加到第一个
                            mRootView.getListDatas().add(0, mRootView.getListDatas().get(i));
                            // 加上 header 的位置
                            mRootView.getListDatas().remove(i + 1);
                            return 0;
                        }
                    }
                    // 不存在本地对话，直接服务器获取
                    return message.cid;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cid -> {
                    if (cid == 0) {
                        mRootView.refreshData();
                    } else {
                        getSingleConversation(cid);
                    }

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


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

    /**
     * 新对话创建回调
     *
     * @param messageItemBean
     */
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
        if (jpushMessageBean.getType() == null) {
            return;
        }
        switch (jpushMessageBean.getType()) {
            // 推送携带的消息  {"seq":36,"msg_type":0,"cid":1,"mid":338248648800337924,"type":"im",
            // "uid":20} IM 消息通过IM接口 同步，故不需要对 推送消息做处理
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM:
                handleIMPush(jpushMessageBean);
                break;
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_FEED_CONTENT:

            default:
                // 服务器同步未读评论和点赞消息
                handleFlushMessage();
                checkUnreadNotification();
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
            ZBIMClient.getInstance().syncAsc(message.getCid(), message.getSeq() - 1, message.getSeq() + 1, (int) System.currentTimeMillis());
            // 获取推送的信息
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
        if (mUnreadNotiSub != null && !mUnreadNotiSub.isUnsubscribed()) {
            mUnreadNotiSub.unsubscribe();
        }

        mUnreadNotiSub = mRepository.getUnreadNotificationData()
                .observeOn(Schedulers.io())
                .map(data -> {
                    mUnReadNotificaitonBean = data;
                    if (data.getCounts() == null) {
                        return false;
                    }
                    /**
                     * 设置未读数
                     */
                    mItemBeanComment.setUnReadMessageNums(data.getCounts().getUnread_comments_count());
                    mItemBeanDigg.setUnReadMessageNums(data.getCounts().getUnread_likes_count());
                    int pinnedNums = 0;
                    if (data.getPinneds() != null && (data.getPinneds().getFeeds().getCount() + data.getPinneds().getNews().getCount()) > 0) {
                        pinnedNums = data.getPinneds().getFeeds().getCount() + data.getPinneds().getNews().getCount();
                        mItemBeanReview.setUnReadMessageNums(pinnedNums);
                    } else {
                        mItemBeanReview.setUnReadMessageNums(0);

                    }

                    /**
                     * 设置时间
                     */
                    mItemBeanComment.getConversation().setLast_message_time(data.getComments() == null || data.getComments().isEmpty() ? 0 :
                            TimeUtils
                                    .utc2LocalLong(data.getComments().get(0).getTime()));
                    mItemBeanDigg.getConversation().setLast_message_time(data.getLikes() == null || data.getLikes().isEmpty() ? 0 :
                            TimeUtils
                                    .utc2LocalLong(data.getLikes().get(0).getTime()));

                    String feedTime = data.getPinneds().getFeeds().getTime();

                    String newTime = data.getPinneds().getNews().getTime();
                    long reviewTime = 0;
                    if (feedTime != null) {
                        reviewTime = TimeUtils
                                .utc2LocalLong(feedTime);
                    }
                    if (newTime != null && TimeUtils.utc2LocalLong(newTime) > reviewTime) {
                        reviewTime = TimeUtils.utc2LocalLong(newTime);
                    }

                    mItemBeanReview.getConversation().setLast_message_time(reviewTime);

                    /**
                     * 设置提示内容
                     * mContext.getString(R.string.has_no_body)
                     + mContext.getString(R.string.comment_me)
                     */
                    String commentTip = getItemTipStr(data.getComments(), MAX_USER_NUMS_COMMENT);
                    if (!TextUtils.isEmpty(commentTip)) {
                        commentTip += mContext.getString(R.string.comment_me);
                    } else {
                        commentTip = mContext.getString(R.string.has_no_body)
                                + mContext.getString(R.string.comment_me);
                    }
                    mItemBeanComment.getConversation().getLast_message().setTxt(
                            commentTip);

                    String diggTip = getItemTipStr(data.getLikes(), MAX_USER_NUMS_DIGG);
                    if (!TextUtils.isEmpty(diggTip)) {
                        diggTip += mContext.getString(R.string.like_me);
                    } else {
                        diggTip = mContext.getString(R.string.has_no_body)
                                + mContext.getString(R.string.like_me);
                    }
                    mItemBeanDigg.getConversation().getLast_message().setTxt(
                            diggTip);

                    String reviewTip;
                    if (data.getPinneds() != null && pinnedNums > 0) {
                        reviewTip = mContext.getString(R.string.new_apply_data);
                    } else {
                        reviewTip = mContext.getString(R.string.no_apply_data);
                        mItemBeanReview.getConversation().setLast_message_time(0);

                    }
                    mItemBeanReview.getConversation().getLast_message().setTxt(
                            reviewTip);
                    // 更新我的消息提示
                    EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE);
                    checkBottomMessageTip();
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean result) {
                        if (result) {
                            mRootView.updateLikeItemData(mItemBeanDigg);
                        }
                    }
                });
        addSubscrebe(mUnreadNotiSub);
    }

    /**
     * 获取用户文字显示  张三、李四评论了我
     *
     * @param commentsNoti
     * @param maxNum
     * @return
     */
    private String getItemTipStr(List<UnreadCountBean> commentsNoti, int maxNum) {
        StringBuilder stringBuilder = new StringBuilder();
        String dot = mContext.getString(R.string.str_pingyin_dot);
        for (int i = 0; i < commentsNoti.size(); i++) {
            if (i < maxNum) {
                try {
                    if (stringBuilder.toString().contains(commentsNoti.get(i).getUser().getName())) {
                        maxNum++;
                    } else {
                        stringBuilder.append(commentsNoti.get(i).getUser().getName());
                        stringBuilder.append(dot);
                    }
                    // 服务器脏数据导致用户信息为空
                } catch (NullPointerException ignored) {
                }
            } else {
                break;
            }
        }
        String tip = stringBuilder.toString();
        if (tip.endsWith(dot)) {
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
        Subscription subscribe = Observable.just(true)
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
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
                    return isShowMessgeTip;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isShowMessgeTip -> {
                    Fragment containerFragment = mRootView.getCureenFragment().getParentFragment();
                    if (containerFragment != null && containerFragment instanceof MessageContainerFragment) {
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(isShowMessgeTip, 0);
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(mNotificaitonRedDotIsShow, 1);
                    }
                    boolean messageContainerRedDotIsShow = isShowMessgeTip || mNotificaitonRedDotIsShow;
                    EventBus.getDefault().post(messageContainerRedDotIsShow, EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE);

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


    }

}
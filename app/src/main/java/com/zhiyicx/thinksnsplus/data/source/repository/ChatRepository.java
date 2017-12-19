package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChatInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.MAX_RETRY_COUNTS;
import static com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.RETRY_DELAY_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class ChatRepository implements ChatContract.Repository {
    private static final String TAG = "ChatRepository";
    @Inject
    protected Application mContext;
    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    private ChatInfoClient mChatInfoClient;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ChatRepository(ServiceManager serviceManager) {
        mChatInfoClient = serviceManager.getChatInfoClient();
    }

    /**
     * 创建对话
     *
     * @param type 会话类型 `0` 私有会话 `1` 群组会话 `2`聊天室会话
     * @param name 会话名称
     * @param pwd  会话加入密码,type=`0`时该参数无效
     * @param uids 会话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
     * @return
     */
    @Override
    public Observable<Conversation> createConveration(int type, String name, String pwd, String uids) {
        return mChatInfoClient.createConversaiton(type, name, pwd, uids)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 插入或者更新数据库
     *
     * @param conversation 对话信息
     * @return
     */
    @Override
    public boolean insertOrUpdateConversation(Conversation conversation) {
        return ConversationDao.getInstance(mContext).insertOrUpdateConversation(conversation);
    }

    /**
     * 插入或者更新数据库
     *
     * @param data 对话信息
     * @return
     */
    @Override
    public boolean insertOrUpdateMessageItemBean(List<MessageItemBean> data) {
        for (MessageItemBean entity : data) {
            ConversationDao.getInstance(mContext).insertOrUpdateConversation(entity.getConversation());
            mUserInfoBeanGreenDao.insertOrReplace(entity.getUserInfo());
        }
        return true;
    }

    /**
     * 获取对话信息列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<MessageItemBean> getConversionListData(long userId) {
        List<MessageItemBean> messageItemBeens = new ArrayList<>();
        List<Conversation> conversations = ConversationDao.getInstance(mContext).getPrivateAndGroupConversationListbyImUid(userId);
        if (conversations == null || conversations.size() == 0) {
            return messageItemBeens;
        }
        int size = conversations.size();
        for (int i = 0; i < size; i++) {
            Conversation tmp = conversations.get(i);
            Message message = MessageDao.getInstance(mContext).getLastMessageByCid(tmp.getCid());
            if (message != null) {
                tmp.setLast_message(message);
                tmp.setLast_message_time(message.getCreate_time());
            } else { // 去除没有聊天消息的
                continue;
            }
            UserInfoBean toChatUserInfo;
            if (tmp.getType() == ChatType.CHAT_TYPE_PRIVATE) {// 私聊
                try {
                    String[] uidsTmp = tmp.getUsids().split(",");
                    if (Long.parseLong(uidsTmp[0]) != userId) {
                        toChatUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(uidsTmp[0]));
                    } else {
                        toChatUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(uidsTmp[1]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, "对话信息中的 userid 有误");
                    return messageItemBeens;
                }
            } else {// 群聊
                toChatUserInfo = new UserInfoBean();
            }
            int unreadMessageCount = MessageDao.getInstance(mContext).getUnReadMessageCount(tmp.getCid());
            MessageItemBean itemBean = new MessageItemBean();
            itemBean.setUserInfo(toChatUserInfo);
            itemBean.setConversation(tmp);
            itemBean.setUnReadMessageNums(unreadMessageCount);
            messageItemBeens.add(itemBean);
        }
        Collections.sort(messageItemBeens, new Comparator<MessageItemBean>() { // 按最新消息排序
            @Override
            public int compare(MessageItemBean o1, MessageItemBean o2) {
                return (int) (o2.getConversation().getLast_message().getCreate_time() - o1.getConversation().getLast_message().getCreate_time());
            }
        });
        return messageItemBeens;
    }

    /**
     * 获取聊天列表信息
     *
     * @param cid
     * @param creat_time
     * @return
     */
    @Override
    public List<ChatItemBean> getChatListData(int cid, long creat_time) {
        List<ChatItemBean> chatItemBeen = new ArrayList<>();
        List<Message> messages = MessageDao.getInstance(mContext).getMessageListByCidAndCreateTime(cid, creat_time);
        if (messages == null || messages.size() == 0) {
            return chatItemBeen;
        }
        for (int i = 0; i < messages.size(); i++) {
            Message tmp = messages.get(i);
            UserInfoBean toChatUserInfo;
            if (tmp.getUid() == 0) {
                toChatUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
            } else {
                toChatUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache((long) tmp.getUid());
            }
            ChatItemBean itemBean = new ChatItemBean();
            itemBean.setUserInfo(toChatUserInfo);
            itemBean.setLastMessage(tmp);
            chatItemBeen.add(itemBean);
        }
        return chatItemBeen;
    }

    @Override
    public List<ChatItemBean> getChatListDataV2(MessageItemBeanV2 itemBeanV2, String msgId, int pageSize) {
        EMConversation conversation = itemBeanV2.getConversation();
        List<EMMessage> msgs = new ArrayList<>();
        if ("0".equals(msgId)){
            // 表示是第一次拿消息，先取出有的，再给msgId赋值，取历史记录
            msgs = conversation.getAllMessages();
            if (!msgs.isEmpty()){
                msgId = msgs.get(0).getMsgId();
            }
        }
        // 从传过来的msgId开始取出历史消息
        msgs.addAll(0, conversation.loadMoreMsgFromDB(msgId, pageSize));
        if (!msgs.isEmpty()){
            List<ChatItemBean> list = new ArrayList<>();
            for (EMMessage message : msgs){
                ChatItemBean chatItemBean = new ChatItemBean();
                chatItemBean.setMessage(message);
                long userId = Long.parseLong("admin".equals(message.getFrom()) ? "1" : message.getFrom());
                UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                chatItemBean.setUserInfo(userInfoBean);
                list.add(chatItemBean);
            }
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public Observable<List<ChatItemBean>> completeUserInfo(List<ChatItemBean> list) {
        return Observable.just(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(list1 -> {
                    List<Object> users = new ArrayList<>();
                    for (ChatItemBean chatItemBean : list1){
                        if ("admin".equals(chatItemBean.getMessage().getFrom())) {
                            users.add(1L);
                        } else {
                            users.add(chatItemBean.getMessage().getFrom());
                        }
                    }
                    return mUserInfoRepository.getUserInfo(users)
                            .map(userInfoBeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    // 更新数据库
                                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                                }
                                for (int i = 0; i < list1.size(); i++) {
                                    int key;
                                    if ("admin".equals(list1.get(i).getMessage().getFrom())) {
                                        key = 1;
                                    } else {
                                        key = Integer.parseInt(list1.get(i).getMessage().getFrom());
                                    }
                                    list1.get(i).setUserInfo(userInfoBeanSparseArray.get(key));
                                }
                                return list1;
                            });
                });
    }


}

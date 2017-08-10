package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChatInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageRepository implements MessageContract.Repository {
    public static final int MAX_RETRY_COUNTS = 3;//重试次数
    public static final int RETRY_DELAY_TIME = 5;// 重试间隔时间,单位 s
    private ChatInfoClient mChatInfoClient;
    private UserInfoClient mUserInfoClient;
    @Inject
    Application mContext;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MessageRepository(ServiceManager serviceManager) {
        mChatInfoClient = serviceManager.getChatInfoClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
    }

    /**
     * 获取当前用户的对话信息
     *
     * @param user_id 用户 id
     * @return
     */
    @Override
    public Observable<BaseJson<List<MessageItemBean>>> getConversationList(final int user_id) {

        return mChatInfoClient.getConversaitonList()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<List<Conversation>>, Observable<BaseJson<List<MessageItemBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<MessageItemBean>>> call(final BaseJson<List<Conversation>> listBaseJson) {
                        final BaseJson<List<MessageItemBean>> baseJson = new BaseJson();
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<MessageItemBean> datas = new ArrayList<>();
                            baseJson.setData(datas);
                            List<Object> integers = new ArrayList<>();
                            for (Conversation tmp : listBaseJson.getData()) {
                                MessageItemBean messageItemBean = new MessageItemBean();
                                Message message = MessageDao.getInstance(mContext).getLastMessageByCid(tmp.getCid());
                                if (message != null) {
                                    tmp.setLast_message(message);
                                    tmp.setLast_message_time(message.getCreate_time());
                                } else {
                                    // 去除没有聊天消息的
                                    continue;
                                }
                                tmp.setIm_uid((int) AppApplication.getmCurrentLoginAuth().getUser_id());
                                if (tmp.getType() == Conversation.CONVERSATION_TYPE_PRIVATE) { // 单聊
                                    try {
                                        String[] uidsPair = tmp.getUsids().split(",");
                                        int pair1 = Integer.parseInt(uidsPair[0]);
                                        int pair2 = Integer.parseInt(uidsPair[1]);
                                        tmp.setPair(pair1 > pair2 ? (pair2 + "&" + pair1) : (pair1 + "&" + pair2)); // "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                // 存储对话信息
                                ConversationDao.getInstance(mContext).insertOrUpdateConversation(tmp);
                                String[] uidsTmp = tmp.getUsids().split(",");
                                UserInfoBean userInfoBean = new UserInfoBean();
                                long toChatUser_id = Long.valueOf((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id() + "") ? uidsTmp[1] : uidsTmp[0]));
                                userInfoBean.setUser_id(toChatUser_id);
                                integers.add(toChatUser_id);
                                messageItemBean.setUserInfo(userInfoBean);
                                // 获取未读消息数量
                                int unreadMessageCount = MessageDao.getInstance(mContext).getUnReadMessageCount(tmp.getCid());
                                messageItemBean.setConversation(tmp);
                                messageItemBean.setUnReadMessageNums(unreadMessageCount);
                                baseJson.getData().add(messageItemBean);
                            }
                            return mUserInfoRepository.getUserInfo(integers).
                                    map(userInfoBeanBaseJson -> {
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userInfoBeanBaseJson) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (int i = 0; i < baseJson.getData().size(); i++) {
                                            baseJson.getData().get(i).setUserInfo(userInfoBeanSparseArray.get(baseJson.getData().get(i).getUserInfo().getUser_id().intValue()));
                                        }
                                        // 存储用户信息
                                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBeanBaseJson);
                                        baseJson.setStatus(true);

                                        return baseJson;
                                    });

                        } else

                        {
                            baseJson.setCode(listBaseJson.getCode());
                            baseJson.setStatus(listBaseJson.isStatus());
                            baseJson.setMessage(listBaseJson.getMessage());
                            return Observable.just(baseJson);
                        }
                    }
                })
                //去除没有聊过天的数据
                .map(listBaseJson -> {
                    if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                        int size = listBaseJson.getData().size();
                        for (int i = 0; i < size; i++) {
                            if (listBaseJson.getData().get(i).getConversation().getLast_message() != null && TextUtils.isEmpty(listBaseJson.getData().get(i).getConversation().getLast_message().getTxt())) {
                                listBaseJson.getData().remove(i);
                            }
                        }
                    }
                    return listBaseJson;
                })
                .

                        observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 通过 cid 获取当前对话信息 ，暂未适配群聊
     *
     * @param cid 对话 id
     * @return
     */
    @Override
    public Observable<BaseJson<MessageItemBean>> getSingleConversation(int cid) {
        return mChatInfoClient.getSingleConversaiton(cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .flatMap(new Func1<BaseJson<Conversation>, Observable<BaseJson<MessageItemBean>>>() {
                             @Override
                             public Observable<BaseJson<MessageItemBean>> call(BaseJson<Conversation> conversationBaseJson) {
                                 final BaseJson<MessageItemBean> baseJson = new BaseJson();
                                 if (conversationBaseJson.isStatus()) {
                                     List<Object> integers = new ArrayList<>();
                                     Conversation tmp = conversationBaseJson.getData();
                                     MessageItemBean messageItemBean = new MessageItemBean();
                                     Message message = MessageDao.getInstance(mContext).getLastMessageByCid(tmp.getCid());
                                     if (message != null) {
                                         tmp.setLast_message(message);
                                         tmp.setLast_message_time(message.getCreate_time());
                                     }
                                     tmp.setIm_uid((int) AppApplication.getmCurrentLoginAuth().getUser_id());
                                     if (tmp.getType() == Conversation.CONVERSATION_TYPE_PRIVATE) { // 单聊
                                         try {
                                             String[] uidsPair = tmp.getUsids().split(",");
                                             int pair1 = Integer.parseInt(uidsPair[0]);
                                             int pair2 = Integer.parseInt(uidsPair[1]);
                                             tmp.setPair(pair1 > pair2 ? (pair2 + "&" + pair1) : (pair1 + "&" + pair2)); // "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                     }
                                     // 存储对话信息
                                     ConversationDao.getInstance(mContext).insertOrUpdateConversation(tmp);
                                     String[] uidsTmp = tmp.getUsids().split(",");
                                     UserInfoBean userInfoBean = new UserInfoBean();
                                     for (int i = 0; i < uidsTmp.length; i++) {
                                         long toChatUser_id = Long.valueOf((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id() + "") ? uidsTmp[1] : uidsTmp[0]));
                                         integers.add(toChatUser_id);
                                     }
                                     try {
                                         userInfoBean.setUser_id((Long) integers.get(0) == AppApplication.getmCurrentLoginAuth().getUser_id() ? (Long) integers.get(1) : (Long) integers.get(0));//保存聊天对象的 user_id ，如果是群聊暂不处理

                                     } catch (Exception e) {
                                         e.printStackTrace();
                                     }
                                     messageItemBean.setUserInfo(userInfoBean);
                                     // 获取未读消息数量
                                     int unreadMessageCount = MessageDao.getInstance(mContext).getUnReadMessageCount(tmp.getCid());
                                     messageItemBean.setConversation(tmp);
                                     messageItemBean.setUnReadMessageNums(unreadMessageCount);
                                     baseJson.setData(messageItemBean);


                                     return mUserInfoRepository.getUserInfo(integers).
                                             map(userInfoBeanBaseJson -> {
                                                 SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                 for (UserInfoBean tmpdata : userInfoBeanBaseJson) {
                                                     userInfoBeanSparseArray.put(tmpdata.getUser_id().intValue(), tmpdata);
                                                 }
                                                 baseJson.setStatus(true);
                                                 baseJson.getData().setUserInfo(userInfoBeanSparseArray.get(baseJson.getData().getUserInfo().getUser_id().intValue()));
                                                 // 存储用户信息
                                                 mUserInfoBeanGreenDao.insertOrReplace(userInfoBeanBaseJson);

                                                 return baseJson;
                                             });
                                 } else {
                                     baseJson.setStatus(conversationBaseJson.isStatus());
                                     baseJson.setCode(conversationBaseJson.getCode());
                                     baseJson.setMessage(conversationBaseJson.getMessage());
                                     return Observable.just(baseJson);
                                 }

                             }
                         }

                );
    }

    @Override
    public Observable<Void> ckeckUnreadNotification() {
        return mUserInfoClient.ckeckUnreadNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TSPNotificationBean>> getNotificationList(String notification, String type, Integer limit, Integer offset) {
        return mUserInfoClient.getNotificationList(notification, type, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<TSPNotificationBean>, Observable<List<TSPNotificationBean>>>() {
                    @Override
                    public Observable<List<TSPNotificationBean>> call(List<TSPNotificationBean> datas) {
                        if (datas.isEmpty()) {
                            return Observable.just(datas);
                        }
                        final List<Object> user_ids = new ArrayList<>();
                        String userIds = "";
                        for (TSPNotificationBean tspNotificationBean : datas) {
                            user_ids.add(tspNotificationBean.getUser_id());
                            userIds += tspNotificationBean.getUser_id() + ",";
                        }
                        if (userIds.length() > 1) {
                            userIds = userIds.substring(0, userIds.length() - 1);
                        }
                        return mUserInfoRepository.getUserInfoByIds(userIds)
                                .subscribeOn(Schedulers.io())
                                .map(userInfoBeens -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : userInfoBeens) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (int i = 0; i < datas.size(); i++) {
                                        if (userInfoBeanSparseArray.get((int) datas.get(i).getUser_id()) != null) {
                                            datas.get(i).setUserInfo(userInfoBeanSparseArray.get((int) datas.get(i).getUser_id()));
                                        }
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeens);
                                    return datas;
                                });
                    }
                });
    }

    @Override
    public Observable<TSPNotificationBean> getNotificationDetail(String notificationId) {
        return mUserInfoClient.getNotificationDetail(notificationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<TSPNotificationBean, Observable<TSPNotificationBean>>() {
                    @Override
                    public Observable<TSPNotificationBean> call(TSPNotificationBean tspNotificationBeen) {

                        return mUserInfoRepository.getUserInfoByIds(String.valueOf(tspNotificationBeen.getUser_id()))
                                .subscribeOn(Schedulers.io())
                                .map(userInfoBeens -> {
                                    tspNotificationBeen.setUserInfo(userInfoBeens.get(0));
                                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeens);
                                    return tspNotificationBeen;
                                });
                    }
                });
    }

    @Override
    public Observable<Object> makeNotificationReaded(String notificationId) {
        return mUserInfoClient.makeNotificationReaded(notificationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}

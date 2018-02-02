package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.EasemobClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/29
 * @contact email:648129313@qq.com
 */

public class BaseMessageRepository implements IBaseMessageRepository{

    @Inject
    Application mContext;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;

    protected EasemobClient mClient;

    @Inject
    public BaseMessageRepository(ServiceManager manager) {
        mClient = manager.getEasemobClient();
    }

    @Override
    public Observable<List<MessageItemBeanV2>> getConversationList(int userId) {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return Observable.just(conversations)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(stringEMConversationMap -> {
                    // 先将环信返回的数据转换为model 然后根据type来处理是否需要获取用户信息，如果不需要则直接返回数据
                    List<MessageItemBeanV2> list = new ArrayList<>();
                    for (Map.Entry<String, EMConversation> entry : stringEMConversationMap.entrySet()) {
                        MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                        itemBeanV2.setConversation(entry.getValue());
                        itemBeanV2.setEmKey(entry.getKey());
                        list.add(itemBeanV2);
                    }
                    // 再在第一条插入ts助手，前提是当前消息列表中没有小助手的消息
                    List<SystemConfigBean.ImHelperBean> tsHlepers = mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
                    // 需要手动插入的小助手，本地查找不到会话才插入聊天信息
                    List<SystemConfigBean.ImHelperBean> needAddedHelpers = new ArrayList<>();
                    if (!tsHlepers.isEmpty()) {
                        for (SystemConfigBean.ImHelperBean imHelperBean : tsHlepers){
                            if (EMClient.getInstance().chatManager().getConversation(imHelperBean.getUid()) == null){
                                needAddedHelpers.add(imHelperBean);
                            }
                        }
                        List<MessageItemBeanV2> messageItemBeanList = new ArrayList<>();
                        for (SystemConfigBean.ImHelperBean imHelperBean : needAddedHelpers) {
                            MessageItemBeanV2 tsHelper = new MessageItemBeanV2();
                            tsHelper.setEmKey(String.valueOf(imHelperBean.getUid()));
                            tsHelper.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(imHelperBean.getUid())));
                            // 创建会话的 conversation 要传入用户名 ts+采用用户Id作为用户名，聊天类型 单聊
                            EMConversation conversation =
                                    EMClient.getInstance().chatManager().getConversation(tsHelper.getEmKey(), EMConversation.EMConversationType.Chat, true);
                            // 给这个会话插入一条自定义的消息 文本类型的
                            EMMessage welcomeMsg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            // 消息体
                            EMTextMessageBody textBody = new EMTextMessageBody(mContext.getString(R.string.ts_helper_default_tip));
                            welcomeMsg.addBody(textBody);
                            // 来自 用户名
                            welcomeMsg.setFrom(tsHelper.getEmKey());
                            // 当前时间
                            welcomeMsg.setMsgTime(System.currentTimeMillis());
                            conversation.insertMessage(welcomeMsg);
                            tsHelper.setConversation(conversation);
                            messageItemBeanList.add(tsHelper);
                        }
                        list.addAll(0, messageItemBeanList);
                    }
                    return completeEmConversation(list)
                            .map(list1 -> list1);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<MessageItemBeanV2>> completeEmConversation(List<MessageItemBeanV2> list) {

        return Observable.just(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(list1 -> {
                    List<Object> users = new ArrayList<>();
                    String groupIds = "";
                    for (MessageItemBeanV2 itemBeanV2 : list1) {
                        if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat) {
                            // 单聊处理用户信息，首先过滤掉环信后台的管理员有用户 admin
                            if (!itemBeanV2.getEmKey().equals("admin")) {
                                itemBeanV2.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(itemBeanV2.getEmKey())));
                                if (itemBeanV2.getUserInfo() == null){
                                    users.add(itemBeanV2.getEmKey());
                                }
                            }
                        } else if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.GroupChat){
                            // 群聊
                            groupIds += itemBeanV2.getConversation().conversationId() + ",";
                        }
                    }
                    if (!TextUtils.isEmpty(groupIds)){
                        groupIds = groupIds.substring(0, groupIds.length() - 1);
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("group_ids", groupIds);
                        // 后台处理
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.GET_CHAT_GROUP_INFO, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_GET_GROUP_INFO, groupIds));
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                                (backgroundRequestTaskBean);
                    }
                    if (users.isEmpty()) {
                        return Observable.just(list1);
                    }
                    return mUserInfoRepository.getUserInfo(users)
                            .map(userInfoBeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (int i = 0; i < list1.size(); i++) {
                                    // 只有单聊才给用户信息
                                    if (list1.get(i).getConversation().getType() == EMConversation.EMConversationType.Chat) {
                                        int key;
                                        if ("admin".equals(list1.get(i).getEmKey())) {
                                            key = 1;
                                        } else {
                                            key = Integer.parseInt(list1.get(i).getEmKey());
                                        }
                                        list1.get(i).setUserInfo(userInfoBeanSparseArray.get(key));
                                    }
                                }
                                return list1;
                            });
                });
    }

    @Override
    public Observable<List<ChatGroupBean>> getGroupInfo(String ids) {
        return mClient.getGroupInfo(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

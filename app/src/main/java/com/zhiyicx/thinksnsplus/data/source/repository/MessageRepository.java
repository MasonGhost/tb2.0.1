package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChatInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageContract;

import java.util.ArrayList;
import java.util.List;

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
    private ChatInfoClient mChatInfoClient;
    private Context mContext;
    private UserInfoRepository mUserInfoRepository;
    private UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    public MessageRepository(ServiceManager serviceManager, Context context) {
        super();
        this.mContext = context;
        mChatInfoClient = serviceManager.getChatInfoClient();
        mUserInfoRepository = AppApplication.AppComponentHolder.getAppComponent().userInfoRepository();
        mUserInfoBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao();
    }


    @Override
    public Observable<BaseJson<List<MessageItemBean>>> getMessageList(final int user_id) {

        return mChatInfoClient.getConversaitonList()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<List<Conversation>>, Observable<BaseJson<List<MessageItemBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<MessageItemBean>>> call(final BaseJson<List<Conversation>> listBaseJson) {
                        final BaseJson<List<MessageItemBean>> baseJson = new BaseJson();
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<MessageItemBean> datas = new ArrayList<>();
                            baseJson.setData(datas);
                            List<Long> integers = new ArrayList<>();
                            for (Conversation tmp : listBaseJson.getData()) {
                                MessageItemBean messageItemBean = new MessageItemBean();
                                Message message = MessageDao.getInstance(mContext).getLastMessageByCid(tmp.getCid());
                                if (message != null) {
                                    tmp.setLast_message_text(message.getTxt());
                                    tmp.setLast_message_time(message.getCreate_time());
                                }
                                tmp.setIm_uid(AppApplication.getmCurrentLoginAuth().getUser_id());
                                try {
                                    String[] uidsPair = tmp.getUsids().split("&");
                                    int pair1 = Integer.parseInt(uidsPair[0]);
                                    int pair2 = Integer.parseInt(uidsPair[1]);
                                    tmp.setPair(pair1 > pair2 ? (pair2 + "&" + pair1) : (pair1 + "&" + pair2)); // "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 存储对话信息
                                ConversationDao.getInstance(mContext).insertOrUpdateConversation(tmp);
                                try {
                                    String[] uidsTmp = tmp.getUsids().split(",");
                                    integers.add(Long.valueOf((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id() + "") ? uidsTmp[1] : uidsTmp[0])));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 获取未读消息数量
                                int unreadMessageCount = MessageDao.getInstance(mContext).getUnReadMessageCount(tmp.getCid());
                                messageItemBean.setConversation(tmp);
                                messageItemBean.setUnReadMessageNums(unreadMessageCount);
                                baseJson.getData().add(messageItemBean);
                            }
                            return mUserInfoRepository.getUserInfo(integers).
                                    map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<MessageItemBean>>>() {
                                        @Override
                                        public BaseJson<List<MessageItemBean>> call(BaseJson<List<UserInfoBean>> userInfoBeanBaseJson) {
                                            baseJson.setStatus(userInfoBeanBaseJson.isStatus());
                                            baseJson.setCode(userInfoBeanBaseJson.getCode());
                                            baseJson.setMessage(userInfoBeanBaseJson.getMessage());
                                            for (int i = 0; i < userInfoBeanBaseJson.getData().size(); i++) {
                                                baseJson.getData().get(i).setUserInfo(userInfoBeanBaseJson.getData().get(i));
                                            }
                                            // 存储用户信息
                                            mUserInfoBeanGreenDao.insertOrReplace(userInfoBeanBaseJson.getData());
                                            return baseJson;

                                        }
                                    });

                        } else {
                            baseJson.setCode(listBaseJson.getCode());
                            baseJson.setStatus(listBaseJson.isStatus());
                            baseJson.setMessage(listBaseJson.getMessage());
                            return Observable.just(baseJson);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }
}

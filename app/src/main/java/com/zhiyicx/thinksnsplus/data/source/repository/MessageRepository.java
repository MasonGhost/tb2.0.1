package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ChatInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
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
    private UserInfoClient mUserInfoClient;
    private Context mContext;

    public MessageRepository(ServiceManager serviceManager, Context context) {
        super();
        this.mContext = context;
        mChatInfoClient = serviceManager.getChatInfoClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
    }


    @Override
    public Observable<BaseJson<List<MessageItemBean>>> getMessageList(final int user_id) {

        return mChatInfoClient.getConversaitonList()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<List<Conversation>>, Observable<BaseJson<List<MessageItemBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<MessageItemBean>>> call(BaseJson<List<Conversation>> listBaseJson) {
                        if (listBaseJson.isStatus()) {
                            String uids = "";
                            for (Conversation tmp : listBaseJson.getData()) {
                                String[] uidsTmp = tmp.getUsids().split(",");
                                uids += ((uidsTmp[0].equals(AppApplication.getmCurrentLoginAuth().getUser_id())) ? uidsTmp[1] : uidsTmp[0]) + ",";
                            }
                            if (uids.length() > 0) {
                                uids = uids.substring(0, uids.length() - 1);
                            }
//                            return mUserInfoClient.getUserInfo(user_id).
//                                    flatMap(new Func1<BaseJson<UserInfoBean>, Observable<BaseJson<List<MessageItemBean>>>>() {
//                                        @Override
//                                        public Observable<BaseJson<List<MessageItemBean>>> call(BaseJson<UserInfoBean> userInfoBeanBaseJson) {
//                                            return null;
//                                        }
//                                    });
                            return Observable.just(listBaseJson)
                                    .map(new Func1<BaseJson<List<Conversation>>, BaseJson<List<MessageItemBean>>>() {
                                        @Override
                                        public BaseJson<List<MessageItemBean>> call(BaseJson<List<Conversation>> listBaseJson) {
                                            BaseJson<List<MessageItemBean>> baseJson = new BaseJson();
                                            baseJson.setStatus(listBaseJson.isStatus());
                                            List<MessageItemBean> datas = new ArrayList<MessageItemBean>();
                                            for (Conversation conversation : listBaseJson.getData()) {
                                                MessageItemBean messageItemBean = new MessageItemBean();
                                                messageItemBean.setConversation(conversation);
                                                messageItemBean.setUserInfo(new UserInfoBean());
                                                datas.add(messageItemBean);
                                            }
                                            baseJson.setData(datas);
                                            return baseJson;
                                        }
                                    })
                                    ;
                        } else {
                            return Observable.empty();
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread());


//        return Observable.just(user_id)
//                .map(new Func1<Integer, BaseJson<List<MessageItemBean>>>() {
//                    @Override
//                    public BaseJson<List<MessageItemBean>> call(Integer integer) {
//                        final List<MessageItemBean> data = new ArrayList<>();
//                        Conversation likeMessage = new Conversation();
//                        likeMessage.setLast_message_text("一叶之秋、晴天色"
//                                + mContext.getString(R.string.like_me));
//                        UserInfoBean userinfo = new UserInfoBean();
//                        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
//                                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
//                        likeMessage.setLast_message_time(System.currentTimeMillis());
//                        for (int i = 0; i < 10; i++) {
//                            MessageItemBean test = new MessageItemBean();
//                            UserInfoBean testUserinfo = new UserInfoBean();
//                            testUserinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com" +
//                                    "%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
//                            testUserinfo.setName("颤三");
//                            testUserinfo.setUser_id((long) (10 + i));
//                            test.setUserInfo(testUserinfo);
//                            Message testMessage = new Message();
//                            testMessage.setTxt("一叶之秋、晴天色" + i
//                                    + mContext.getString(R.string.like_me));
//                            testMessage.setCreate_time(System.currentTimeMillis());
//                            test.setConversation(likeMessage);
//                            test.setUnReadMessageNums((int) (Math.random() * 10));
//                            data.add(test);
//                        }
//                        BaseJson baseJson = new BaseJson();
//                        baseJson.setStatus(true);
//                        baseJson.setData(data);
//                        return baseJson;
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
    }
}

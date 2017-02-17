package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
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
    private CommonClient mCommonClient;
    private Context mContext;

    public MessageRepository(ServiceManager serviceManager, Context context) {
        super();
        this.mContext = context;
        mCommonClient = serviceManager.getCommonClient();

    }


    @Override
    public Observable<BaseJson<List<MessageItemBean>>> getMessageList(int user_id) {
        return Observable.just(user_id)
                .map(new Func1<Integer, BaseJson<List<MessageItemBean>>>() {
                    @Override
                    public BaseJson<List<MessageItemBean>> call(Integer integer) {
                        final List<MessageItemBean> data = new ArrayList<>();
                        Conversation likeMessage = new Conversation();
                        likeMessage.setLast_message_text("一叶之秋、晴天色"
                                + mContext.getString(R.string.like_me));
                        UserInfoBean userinfo = new UserInfoBean();
                        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
                                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
                        likeMessage.setLast_message_time(System.currentTimeMillis());
                        for (int i = 0; i < 10; i++) {
                            MessageItemBean test = new MessageItemBean();
                            UserInfoBean testUserinfo = new UserInfoBean();
                            testUserinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com" +
                                    "%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
                            testUserinfo.setName("颤三");
                            testUserinfo.setUser_id((long) (10 + i));
                            test.setUserInfo(testUserinfo);
                            Message testMessage = new Message();
                            testMessage.setTxt("一叶之秋、晴天色" + i
                                    + mContext.getString(R.string.like_me));
                            testMessage.setCreate_time(System.currentTimeMillis());
                            test.setConversation(likeMessage);
                            test.setUnReadMessageNums((int) (Math.random() * 10));
                            data.add(test);
                        }
                        BaseJson baseJson = new BaseJson();
                        baseJson.setStatus(true);
                        baseJson.setData(data);
                        return baseJson;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class ChatRepository implements ChatContract.Repository {
    private static final String TAG = "ChatRepository";
    private final UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    private UserInfoClient mUserInfoClient;
    private Context mContext;

    public ChatRepository(ServiceManager serviceManager, Application context) {
        super();
        mContext = context;
        mUserInfoClient = serviceManager.getUserInfoClient();
        mUserInfoBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .userInfoBeanGreenDao();
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
    public Observable<BaseJson<Conversation>> createConveration(int type, String name, String pwd, String uids) {
        return mUserInfoClient.createConversaiton(type, name, pwd, uids)
                .subscribeOn(Schedulers.io())
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

    @Override
    public List<MessageItemBean> getConversionListData(long userId) {
        List<MessageItemBean> messageItemBeens = new ArrayList<>();
        UserInfoBean toChatUserInfo;
        List<Conversation> conversations = ConversationDao.getInstance(mContext).getConversationListbyImUid(userId);
        if (conversations == null || conversations.size() == 0) {
            return messageItemBeens;
        }
        try {
            toChatUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(conversations.get(0).getUsids().split(",")[1]));

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "对话信息中的 userid 有误");
            return messageItemBeens;
        }
        for (int i = 0; i < conversations.size(); i++) {
            MessageItemBean itemBean = new MessageItemBean();
            itemBean.setUserInfo(toChatUserInfo);
            itemBean.setConversation(conversations.get(i));
            messageItemBeens.add(itemBean);
        }
        return messageItemBeens;
    }

    @Override
    public List<ChatItemBean> getChatListData(int cid, long mid) {
        return new ArrayList<>();
    }


}

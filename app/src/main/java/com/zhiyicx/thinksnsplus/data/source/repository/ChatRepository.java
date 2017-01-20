package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

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
    private CommonClient mCommonClient;
    private UserInfoClient mUserInfoClient;

    public ChatRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
        mUserInfoClient=serviceManager.getUserInfoClient();
    }

    /**
     * 创建对话
     * @param type 会话类型 `0` 私有会话 `1` 群组会话 `2`聊天室会话
     * @param name 会话名称
     * @param pwd  会话加入密码,type=`0`时该参数无效
     * @param uids 会话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
     * @return
     */
    @Override
    public Observable<BaseJson<Conversation>> createConveration(String type, String name, String pwd, String uids) {
        return mUserInfoClient.createConversaiton(type,name,pwd,uids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

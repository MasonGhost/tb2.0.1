package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoRepository extends BaseFriendsRepository implements ChatInfoContract.Repository {

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public ChatInfoRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId) {
        return mEasemobClient.getGroupInfo(groupId);
    }
}

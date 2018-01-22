package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoRepository extends BaseFriendsRepository implements ChatInfoContract.Repository{

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public ChatInfoRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<ChatGroupBean> updateGroup(String im_group_id, String groupName, String groupIntro, int isPublic,
                                                 int maxUser, boolean isMemberOnly, int isAllowInvites, String groupFace) {

        return mUpLoadRepository.upLoadSingleFileV2(groupFace, "", true, 0,0)
                .flatMap(integerBaseJson -> mEasemobClient.updateGroup(im_group_id, groupName, groupIntro, isPublic, maxUser, isMemberOnly, isAllowInvites,String.valueOf(integerBaseJson.getData()))
                        .flatMap(chatGroupBean -> Observable.just(chatGroupBean)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())));
    }
}

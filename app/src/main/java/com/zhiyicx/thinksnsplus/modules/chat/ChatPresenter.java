package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends AppBasePresenter<ChatContract.View> implements ChatContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;


    @Inject
    public ChatPresenter(ChatContract.View rootView) {
        super(rootView);
    }

    @Override
    public void dealMessages(List<EMMessage> messages) {
        Observable.just(messages)
                .subscribeOn(Schedulers.io())
                .flatMap(emMessages -> {
                    List<Object> userIds = new ArrayList<>();
                    for (EMMessage msg : emMessages) {
                        Long userId = null;
                        try {
                            userId = Long.parseLong(msg.getFrom());
                        } catch (NumberFormatException ignore) {
                        }
                        if (userId != null) {
                            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                            if (userInfoBean == null) {
                                userIds.add(userId);
                            }
                        }
                    }
                    if (userIds.isEmpty()) {
                        return Observable.just(emMessages);
                    } else {
                        return mUserInfoRepository.getUserInfo(userIds)
                                .flatMap(userInfoBeans -> Observable.just(emMessages));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<EMMessage>>() {
                    @Override
                    protected void onSuccess(List<EMMessage> data) {
                        mRootView.onMessageReceivedWithUserInfo(data);
                    }
                });
    }

    @Override
    public String getUserName(String id) {
        return mUserInfoBeanGreenDao.getUserName(id);
    }

    @Override
    public String getGroupName(String id) {
        try {
            return mChatGroupBeanGreenDao.getChatGroupName(id);
        } catch (Exception e) {
            return "未知用户";
        }
    }

    /**
     * @param id    群 id
     * @param count 变动 数量
     * @param add   是否 加法
     * @return
     */
    @Override
    public boolean updateChatGroupMemberCount(String id, int count, boolean add) {
        return mChatGroupBeanGreenDao.updateChatGroupMemberCount(id, count, add);
    }
}

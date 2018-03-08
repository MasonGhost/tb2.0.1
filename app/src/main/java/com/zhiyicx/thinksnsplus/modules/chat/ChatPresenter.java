package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
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
    BaseFriendsRepository mBaseFriendsRepository;
    @Inject
    ChatInfoRepository mRepository;

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
    public void getGroupChatInfo(String groupId) {
        Subscription subscription = mRepository.getGroupChatInfo(groupId)
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mChatGroupBeanGreenDao.saveMultiData(data);
                        if (!data.isEmpty()) {
                            mRootView.setTitle(data.get(0).getName() + "(" + data.get(0).getAffiliations_count() + ")");
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateGroupName(ChatGroupBean chatGroupBean) {
        if (chatGroupBean == null) {
            return;
        }
        Subscription subscription = mBaseFriendsRepository.updateGroup(chatGroupBean.getId(), chatGroupBean.getName(), chatGroupBean.getDescription(), 0, 200, chatGroupBean.isMembersonly(),
                0, chatGroupBean.getGroup_face(), false, "")
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("修改中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        mChatGroupBeanGreenDao.saveSingleData(chatGroupBean);
                        mRootView.setGoupName(data.getName() + "(" + chatGroupBean.getAffiliations_count() + ")");
                        mRootView.dismissSnackBar();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public String getUserName(String id) {
        return mUserInfoBeanGreenDao.getUserName(id);
    }

    @Override
    public UserInfoBean getUserInfo(String id) {
        return mUserInfoBeanGreenDao.getUserInfoById(id);
    }

    @Override
    public String getGroupName(String id) {
        try {
            return mChatGroupBeanGreenDao.getChatGroupName(id);
        } catch (Exception e) {
            return "未知用户";
        }
    }

    @Override
    public ChatGroupBean getChatGroupInfo(String id) {
        return mChatGroupBeanGreenDao.getChatGroupBeanById(id);
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

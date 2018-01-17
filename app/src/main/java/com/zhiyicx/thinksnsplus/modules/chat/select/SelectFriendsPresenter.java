package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2.BUNDLE_CHAT_DATA;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsPresenter extends AppBasePresenter<SelectFriendsContract.Repository, SelectFriendsContract.View>
        implements SelectFriendsContract.Presenter{

    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public SelectFriendsPresenter(SelectFriendsContract.Repository repository, SelectFriendsContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getUserFriendsList(maxId, "")
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        if (!data.isEmpty()){
                            for (UserInfoBean userInfoBean : data){
                                userInfoBean.setSelected(false);
                            }
                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, throwable.getMessage());
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        List<UserInfoBean> followFansBeanList = mUserInfoBeanGreenDao.getUserFriendsList(maxId);
        for (UserInfoBean userInfoBean : followFansBeanList){
            userInfoBean.setSelected(false);
        }
        mRootView.onCacheResponseSuccess(followFansBeanList, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getFriendsListByKey(Long maxId, String key) {
        Subscription subscription = mRepository.getUserFriendsList(maxId, key)
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        for (UserInfoBean userInfoBean : data){
                            userInfoBean.setSelected(false);
                        }
                        mRootView.getFriendsListByKeyResult(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, throwable.getMessage());
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void createConversation(List<UserInfoBean> list) {
        // 没有添加当前用户的情况下 添加在第一个
        if (list.get(0).getUser_id() != AppApplication.getMyUserIdWithdefault()){
            list.add(0, mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        }
        if (list.size() == 2){
            String id = String.valueOf(list.get(0).getUser_id());
            // 创建单聊，判断当前是否与该用户的会话，没有创建会话
            mRootView.createConversionResult(getChatUser(list), EMConversation.EMConversationType.Chat, EaseConstant.CHATTYPE_SINGLE, id);
        } else {
            // 创建群组会话
            String groupName = list.get(0).getName() + "、" + list.get(1).getName();
            String groupIntro = "暂无";
            StringBuilder members = new StringBuilder();
            for (UserInfoBean userInfoBean : list){
                members.append(String.valueOf(userInfoBean.getUser_id())).append(",");
            }
            Subscription subscription = mRepository.createGroup(groupName, groupIntro, false,
                    200, true, false, list.get(0).getUser_id(), members.substring(0, members.length() - 1))
                    .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                        @Override
                        protected void onSuccess(ChatGroupBean data) {
                            // 创建成功 跳转聊天详情页面
                            String id = data.getIm_group_id();
                            mRootView.createConversionResult(getChatUser(list), EMConversation.EMConversationType.GroupChat, EaseConstant.CHATTYPE_GROUP, id);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showSnackErrorMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.showSnackErrorMessage(throwable.getMessage());
                        }
                    });
            addSubscrebe(subscription);
        }
    }

    private List<ChatUserInfoBean> getChatUser(List<UserInfoBean> userInfoBeanList){
        List<ChatUserInfoBean> list = new ArrayList<>();
        for (UserInfoBean userInfoBean : userInfoBeanList){
            ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
            chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
            chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
            chatUserInfoBean.setName(userInfoBean.getName());
            chatUserInfoBean.setSex(userInfoBean.getSex());
            if (userInfoBean.getVerified() != null){
                ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
                verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
                verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
                verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
                verifiedBean.setType(userInfoBean.getVerified().getType());
                chatUserInfoBean.setVerified(verifiedBean);
            }
            list.add(chatUserInfoBean);
        }
        return list;
    }
}

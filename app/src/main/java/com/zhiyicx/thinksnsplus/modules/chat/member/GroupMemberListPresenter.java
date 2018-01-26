package com.zhiyicx.thinksnsplus.modules.chat.member;

import android.os.Bundle;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.GroupMemberListRepository;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_REMOVE_MEMBER;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListPresenter extends AppBasePresenter< GroupMemberListContract.View>
        implements GroupMemberListContract.Presenter{

    @Inject
    GroupMemberListRepository mGroupMemberListRepository;

    @Inject
    public GroupMemberListPresenter( GroupMemberListContract.View rootView) {
        super(rootView);
    }

    @Override
    public boolean isOwner() {
        return mRootView.getGroupData().getOwner() == AppApplication.getMyUserIdWithdefault();
    }

    @Subscriber(tag = EVENT_IM_GROUP_REMOVE_MEMBER)
    public void onGroupMemberRemoved(Bundle bundle){
        List<UserInfoBean> removedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_REMOVE_MEMBER);
        if (removedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupData();
        List<UserInfoBean> originalList = new ArrayList<>();
        originalList.addAll(chatGroupBean.getAffiliations());
        for (int i = 0; i < removedList.size(); i++) {
            for (UserInfoBean userInfoBean : chatGroupBean.getAffiliations()){
                if (removedList.get(i).getUser_id().equals(userInfoBean.getUser_id())){
                    originalList.remove(userInfoBean);
                    break;
                }
            }
        }
        chatGroupBean.setAffiliations(originalList);
        mRootView.updateGroup(chatGroupBean);
    }

    @Subscriber(tag = EVENT_IM_GROUP_ADD_MEMBER)
    public void onGroupMemberAdded(Bundle bundle){
        List<UserInfoBean> addedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_ADD_MEMBER);
        if (addedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupData();
        chatGroupBean.getAffiliations().addAll(addedList);
        mRootView.updateGroup(chatGroupBean);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}

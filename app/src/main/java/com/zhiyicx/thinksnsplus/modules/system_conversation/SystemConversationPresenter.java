package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.util.SparseArray;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationPresenter extends BasePresenter<SystemConversationContract.Repository, SystemConversationContract.View> implements SystemConversationContract.Presenter {

    private SparseArray<UserInfoBean> mUserInfoBeanSparseArray = new SparseArray<>();// 把用户信息存入内存，方便下次使用


    @Inject
    public SystemConversationPresenter(SystemConversationContract.Repository repository, SystemConversationContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void sendTextMessage(String text, int cid) {

    }

    @Override
    public void reSendText(ChatItemBean chatItemBean) {

    }
}

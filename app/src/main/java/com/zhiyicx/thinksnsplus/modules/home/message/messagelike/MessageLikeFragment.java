package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;

/**
 * @Describe 消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageLikeFragment extends TSListFragment<MessageLikeContract.Presenter, DigedBean> implements MessageLikeContract.View {

    public MessageLikeFragment() {
    }

    public static MessageLikeFragment newInstance() {
        MessageLikeFragment fragment = new MessageLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.like);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected CommonAdapter<DigedBean> getAdapter() {
        return new MessageLikeAdapter(getActivity(), R.layout.item_message_like_list, mListDatas);
    }


}

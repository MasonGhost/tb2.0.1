package com.zhiyicx.thinksnsplus.modules.chat.member;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListFragment extends TSFragment<GroupMemberListContract.Presenter> implements GroupMemberListContract.View {

    public static final String BUNDLE_GROUP_MEMBER = "bundle_group_member";

    @BindView(R.id.rv_member_list)
    RecyclerView mRvMemberList;

    private ChatGroupBean mChatGroupBean;

    public GroupMemberListFragment instance(Bundle bundle) {
        GroupMemberListFragment fragment = new GroupMemberListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mRvMemberList.setLayoutManager(manager);

    }

    @Override
    protected void initData() {
        mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_MEMBER);
        ChatMemberAdapter adapter = new ChatMemberAdapter(getContext(), mChatGroupBean.getAffiliations());
        List<UserInfoBean> userInfoBeans = new ArrayList<>();
        userInfoBeans.addAll(mChatGroupBean.getAffiliations());
        // 添加按钮，都可以拉人
        UserInfoBean chatUserInfoBean = new UserInfoBean();
        chatUserInfoBean.setUser_id(-1L);
        userInfoBeans.add(chatUserInfoBean);
        if (mPresenter.isOwner()) {
            // 删除按钮，仅群主
            UserInfoBean chatUserInfoBean1 = new UserInfoBean();
            chatUserInfoBean1.setUser_id(-2L);
            userInfoBeans.add(chatUserInfoBean1);
        }
        mRvMemberList.setAdapter(adapter);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_group_member_list;
    }

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }
}

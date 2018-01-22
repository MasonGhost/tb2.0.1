package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoFragment extends TSFragment<ChatInfoContract.Presenter> implements ChatInfoContract.View {

    @BindView(R.id.iv_user_portrait)
    UserAvatarView mIvUserPortrait;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.iv_add_user)
    ImageView mIvAddUser;
    @BindView(R.id.ll_single)
    LinearLayout mLlSingle;
    @BindView(R.id.rv_member_list)
    RecyclerView mRvMemberList;
    @BindView(R.id.tv_to_all_members)
    TextView mTvToAllMembers;
    @BindView(R.id.ll_group)
    LinearLayout mLlGroup;
    @BindView(R.id.ll_manager)
    LinearLayout mLlManager;
    @BindView(R.id.tv_clear_message)
    TextView mTvClearMessage;
    @BindView(R.id.tv_delete_group)
    TextView mTvDeleteGroup;
    @BindView(R.id.iv_group_portrait)
    ImageView mIvGroupPortrait;
    @BindView(R.id.tv_group_name)
    TextView mTvGroupName;

    private int mChatType;
    public List<ChatUserInfoBean> mUserInfoBeans;
    private String mChatId;

    public ChatInfoFragment instance(Bundle bundle) {
        ChatInfoFragment fragment = new ChatInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mUserInfoBeans = getArguments().getParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST);
        mChatType = getArguments().getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        mChatId = getArguments().getString("id");
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 屏蔽群聊的布局
            mLlGroup.setVisibility(View.GONE);
            mLlManager.setVisibility(View.GONE);
            mTvDeleteGroup.setVisibility(View.GONE);
        } else {
            // 屏蔽单聊的布局
            mLlSingle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 单聊处理布局
            ImageUtils.loadUserHead(mUserInfoBeans.get(1), mIvUserPortrait, false);
            mTvUserName.setText(mUserInfoBeans.get(1).getName());
        } else {
            // 群聊
            EMGroup group = EMClient.getInstance().groupManager().getGroup(mChatId);
            mTvGroupName.setText(group.getGroupName());
        }

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_info_title);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat_info;
    }

    @OnClick({R.id.iv_add_user, R.id.tv_to_all_members, R.id.ll_manager, R.id.tv_clear_message, R.id.tv_delete_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_user:
                break;
            case R.id.tv_to_all_members:
                break;
            case R.id.ll_manager:
                break;
            case R.id.tv_clear_message:
                break;
            case R.id.tv_delete_group:
                break;
            default:
        }
    }
}

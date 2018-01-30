package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.edit.manager.GroupManagerActivity;
import com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameActivity;
import com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerActivity;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListActivity;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;
import static com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2.BUNDLE_CHAT_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_NAME;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerFragment.BUNDLE_GROUP_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListFragment.BUNDLE_GROUP_MEMBER;
import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_EDIT_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_IS_DELETE;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoFragment extends TSFragment<ChatInfoContract.Presenter> implements ChatInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

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
    @BindView(R.id.sc_block_message)
    SwitchCompat mScBlockMessage;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    @BindView(R.id.rl_block_message)
    RelativeLayout mRlBlockMessage;

    private int mChatType;
    public List<ChatUserInfoBean> mUserInfoBeans;
    private String mChatId;

    // 删除群聊
    private ActionPopupWindow mDeleteGroupPopupWindow;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;
    private ChatGroupBean mChatGroupBean;

    public ChatInfoFragment instance(Bundle bundle) {
        ChatInfoFragment fragment = new ChatInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        // 初始化图片选择器
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build().photoSelectorImpl();
        mUserInfoBeans = getArguments().getParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST);
        mChatType = getArguments().getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        mChatId = getArguments().getString("id");
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 屏蔽群聊的布局
            mLlGroup.setVisibility(View.GONE);
            mLlManager.setVisibility(View.GONE);
            mTvDeleteGroup.setVisibility(View.GONE);
            isShowEmptyView(false, true);
            setGroupData();
            setCenterText(getString(R.string.chat_info_title_single));
            // 单聊没有屏蔽消息
            mRlBlockMessage.setVisibility(View.GONE);
        } else {
            mPresenter.getGroupChatInfo(mChatId);
            // 屏蔽单聊的布局
            mLlSingle.setVisibility(View.GONE);
        }
        initDeletePopupWindow();
        initPhotoPopupWindow();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_info_title);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat_info;
    }

    @OnClick({R.id.iv_add_user, R.id.tv_to_all_members, R.id.ll_manager, R.id.tv_clear_message, R.id.tv_delete_group,
            R.id.ll_group_portrait, R.id.ll_group_name, R.id.iv_user_portrait})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_user:
                // 添加成员
                mPresenter.createGroupFromSingleChat();
                break;
            case R.id.tv_to_all_members:
                // 查看所有成员
                Intent intentAllMember = new Intent(getContext(), GroupMemberListActivity.class);
                Bundle bundleAllMember = new Bundle();
                bundleAllMember.putParcelable(BUNDLE_GROUP_MEMBER, mChatGroupBean);
                intentAllMember.putExtras(bundleAllMember);
                startActivity(intentAllMember);
                break;
            case R.id.ll_manager:
                // 跳转群管理
                Intent intent = new Intent(getContext(), GroupManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_GROUP_DATA, mChatGroupBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_clear_message:
                // 清空消息记录
                EMClient.getInstance().chatManager().getConversation(mChatId).clearAllMessages();
                break;
            case R.id.tv_delete_group:
                // （群主）删除群聊
                mDeleteGroupPopupWindow.show();
                break;
            case R.id.ll_group_portrait:
                // 修改群头像
                if (mChatType == ChatConfig.CHATTYPE_GROUP && mPresenter.isGroupOwner()) {
                    mPhotoPopupWindow.show();
                }
                break;
            case R.id.ll_group_name:
                // 修改群名称
                if (mChatType == ChatConfig.CHATTYPE_GROUP && mPresenter.isGroupOwner()) {
                    Intent intentName = new Intent(getContext(), EditGroupNameActivity.class);
                    Bundle bundleName = new Bundle();
                    bundleName.putString(GROUP_ORIGINAL_NAME, mChatGroupBean.getName());
                    intentName.putExtras(bundleName);
                    startActivity(intentName);
                }
                break;
            case R.id.iv_user_portrait:
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setUser_id(mUserInfoBeans.get(1).getUser_id());
                PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
                break;
            default:
        }
    }

    private void initDeletePopupWindow() {
        if (mDeleteGroupPopupWindow == null) {
            mDeleteGroupPopupWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.prompt))
                    .item2Str(mPresenter.isGroupOwner() ? getString(R.string.chat_delete) : getString(R.string.chat_quit))
                    .desStr(mPresenter.isGroupOwner() ? getString(R.string.chat_delete_group_alert) : getString(R.string.chat_quit_group_alert))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item2ClickListener(() -> {
                        try {
                            if (mPresenter.isGroupOwner()) {
                                // 解散群组
                                EMClient.getInstance().groupManager().destroyGroup(mChatId);
                            } else {
                                // 退群
                                EMClient.getInstance().groupManager().leaveGroup(mChatId);
                            }
                            mDeleteGroupPopupWindow.hide();
                            EventBus.getDefault().post(mChatId, EVENT_IM_DELETE_QUIT);
                            getActivity().finish();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            showSnackErrorMessage("操作失败");
                        }
                    })
                    .bottomClickListener(() -> mDeleteGroupPopupWindow.hide())
                    .build();
        }
    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(mActivity.getString(R.string.choose_from_photo))
                .item2Str(mActivity.getString(R.string.choose_from_camera))
                .bottomStr(mActivity.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(mActivity)
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    public String getChatId() {
        return mChatId;
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean) {
        // emm 由于没有完全返回所有信息 再加上字段也不同 所以手动改一下
        mChatGroupBean.setGroup_face(chatGroupBean.getGroup_face());
        mChatGroupBean.setPublic(chatGroupBean.isPublic());
        String groupName = TextUtils.isEmpty(chatGroupBean.getName()) ? chatGroupBean.getName() : chatGroupBean.getName();
        mChatGroupBean.setName(groupName);
        mChatGroupBean.setDescription(chatGroupBean.getDescription());
        mChatGroupBean.setMembersonly(chatGroupBean.isMembers_only());
        mChatGroupBean.setAllowinvites(chatGroupBean.isAllowinvites());
        setGroupData();
    }

    @Override
    public void getGroupInfoSuccess(ChatGroupBean chatGroupBean) {
        mChatGroupBean = chatGroupBean;
        mChatGroupBean.setIm_group_id(mChatId);
        setGroupData();
        // 切换是否屏蔽消息
        mScBlockMessage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
                if (isChecked) {

                }
            } else {
                try {
                    if (isChecked) {
                        EMClient.getInstance().groupManager().blockGroupMessage(mChatId);
                    } else {
                        EMClient.getInstance().groupManager().unblockGroupMessage(mChatId);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    showSnackErrorMessage("操作失败");
                }
            }
        });
    }

    @Override
    public ChatGroupBean getGroupBean() {
        return mChatGroupBean;
    }

    @Override
    public void isShowEmptyView(boolean isShow, boolean isSuccess) {
        mLlContainer.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mEmptyView.setErrorType(isShow ? EmptyView.STATE_NETWORK_LOADING : EmptyView.STATE_HIDE_LAYOUT);
        if (!isSuccess) {
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
        }
    }

    @Override
    public String getToUserId() {
        return mUserInfoBeans.size() == 2 ? mUserInfoBeans.get(1).getUser_id() + "" : "";
    }

    @Override
    public void createGroupSuccess(ChatGroupBean chatGroupBean) {
        String id = !TextUtils.isEmpty(chatGroupBean.getIm_group_id()) ?
                chatGroupBean.getIm_group_id() : chatGroupBean.getId();
        if (EMClient.getInstance().groupManager().getGroup(id) == null) {
            // 不知道为啥 有时候获取不到群组对象
            showSnackErrorMessage("创建失败");
        } else {
            // 点击跳转聊天
            Intent to = new Intent(getContext(), ChatActivityV2.class);
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.EXTRA_USER_ID, id);
            bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
            bundle.putParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST,
                    (ArrayList<? extends Parcelable>) mUserInfoBeans);
            to.putExtra(BUNDLE_CHAT_DATA, bundle);
            startActivity(to);
            getActivity().finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        mChatGroupBean.setGroup_face(photoList.get(0).getImgUrl());
        mPresenter.updateGroup(mChatGroupBean, true);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        showSnackErrorMessage(errorMsg);
    }

    private void setGroupData() {
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 单聊处理布局
            ImageUtils.loadUserHead(mUserInfoBeans.get(1), mIvUserPortrait, false);
            mTvUserName.setText(mUserInfoBeans.get(1).getName());
            mIvUserPortrait.setOnClickListener(v -> {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setUser_id(mUserInfoBeans.get(1).getUser_id());
                PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
            });
        } else {
            // 非群主屏蔽群管理
            if (!mPresenter.isGroupOwner()) {
                mLlManager.setVisibility(View.GONE);
                mTvDeleteGroup.setText(getString(R.string.chat_quit_group));
            } else {
                // 群主无法屏蔽消息
                mRlBlockMessage.setVisibility(View.GONE);
            }
            // 群聊的信息展示
            EMGroup group = EMClient.getInstance().groupManager().getGroup(mChatId);
            // 屏蔽按钮
            mScBlockMessage.setChecked(group.isMsgBlocked());
            // 群名称
            String groupName = mChatGroupBean.getName()
                    + "(" + mChatGroupBean.getAffiliations_count() + ")";
            mTvGroupName.setText(groupName);
            // 群头像
            Glide.with(getContext())
                    .load(mChatGroupBean.getGroup_face() + "")
                    .override(25, 25)
                    .error(R.mipmap.ico_ts_assistant)
                    .placeholder(R.mipmap.ico_ts_assistant)
                    .into(mIvGroupPortrait);
            // 成员列表
            RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 5);
            mRvMemberList.setLayoutManager(manager);
            mRvMemberList.addItemDecoration(new GridDecoration(10, 10));
            List<UserInfoBean> list = new ArrayList<>();
            list.addAll(mChatGroupBean.getAffiliations());
            // 添加按钮，都可以拉人
            UserInfoBean chatUserInfoBean = new UserInfoBean();
            chatUserInfoBean.setUser_id(-1L);
            list.add(chatUserInfoBean);
            if (mPresenter.isGroupOwner()) {
                // 删除按钮，仅群主
                UserInfoBean chatUserInfoBean1 = new UserInfoBean();
                chatUserInfoBean1.setUser_id(-2L);
                list.add(chatUserInfoBean1);
            }
            ChatMemberAdapter memberAdapter = new ChatMemberAdapter(getContext(), list, mChatGroupBean.getOwner());
            mRvMemberList.setAdapter(memberAdapter);
            memberAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (list.get(position).getUser_id() == -1L) {
                        // 添加
                        Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                        bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, false);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (list.get(position).getUser_id() == -2L) {
                        // 移除
                        Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                        bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        PersonalCenterFragment.startToPersonalCenter(getContext(), list.get(position));
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }
}

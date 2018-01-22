package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;

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

    private int mChatType;
    public List<ChatUserInfoBean> mUserInfoBeans;
    private String mChatId;

    // 删除群聊
    private ActionPopupWindow mDeleteGroupPopupWindow;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;

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
                        .SHAPE_RCTANGLE))
                .build().photoSelectorImpl();
        mUserInfoBeans = getArguments().getParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST);
        mChatType = getArguments().getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        mChatId = getArguments().getString("id");
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 屏蔽群聊的布局
            mLlGroup.setVisibility(View.GONE);
            mLlManager.setVisibility(View.GONE);
        } else {
            // 屏蔽单聊的布局
            mLlSingle.setVisibility(View.GONE);
            // 非群主屏蔽群管理
            if (!mPresenter.isGroupOwner()){
                mLlManager.setVisibility(View.GONE);
                mTvDeleteGroup.setText(getString(R.string.chat_quit_group));
            }
        }
        initDeletePopupWindow();
        initPhotoPopupWindow();
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
            mScBlockMessage.setChecked(group.isMsgBlocked());
            mTvGroupName.setText(group.getGroupName());
            RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 5);
            mRvMemberList.setLayoutManager(manager);
            List<ChatUserInfoBean> list = new ArrayList<>();
            list.addAll(mUserInfoBeans);
            if (!mPresenter.isGroupOwner()){
                // 添加按钮
                ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
                chatUserInfoBean.setUser_id(-1L);
                // 删除按钮
                ChatUserInfoBean chatUserInfoBean1 = new ChatUserInfoBean();
                chatUserInfoBean1.setUser_id(-2L);
                list.add(chatUserInfoBean);
                list.add(chatUserInfoBean1);
            }
            ChatMemberAdapter memberAdapter = new ChatMemberAdapter(getContext(), list);
            mRvMemberList.setAdapter(memberAdapter);
            memberAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (list.get(position).getUser_id() == -1L){
                        // 添加
                    } else if (list.get(position).getUser_id() == -2L){
                        // 移除
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }

        // 切换是否屏蔽消息
        mScBlockMessage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
                if (isChecked){

                }
            } else {
                try {
                    if (isChecked){
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
    protected String setCenterTitle() {
        return getString(R.string.chat_info_title);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat_info;
    }

    @OnClick({R.id.iv_add_user, R.id.tv_to_all_members, R.id.ll_manager, R.id.tv_clear_message, R.id.tv_delete_group, R.id.ll_group_portrait})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_user:
                // 添加成员
                break;
            case R.id.tv_to_all_members:
                // 查看所有成员
                break;
            case R.id.ll_manager:
                // 跳转群管理
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
                mPhotoPopupWindow.show();
                break;
            default:
        }
    }

    private void initDeletePopupWindow(){
        if (mDeleteGroupPopupWindow == null){
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
                            if (mPresenter.isGroupOwner()){
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
        EMGroup group = EMClient.getInstance().groupManager().getGroup(mChatId);
        mTvGroupName.setText(group.getGroupName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(mChatId);
        String member = "";
        for (ChatUserInfoBean chatUserInfoBean: mUserInfoBeans){
            member += chatUserInfoBean.getUser_id() + ",";
        }
        mPresenter.updateGroup(mChatId, group.getGroupName(), "暂无", 0, 200, true,
                0, photoList.get(0).getImgUrl());
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        showSnackErrorMessage(errorMsg);
    }
}

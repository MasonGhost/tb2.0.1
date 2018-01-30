package com.zhiyicx.thinksnsplus.modules.chat.edit.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerActivity;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerFragment.BUNDLE_GROUP_DATA;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupManagerFragment extends TSFragment<GroupManagerContract.Presenter> implements GroupManagerContract.View {

    @BindView(R.id.sc_block_message)
    SwitchCompat mScOpen;

    private ChatGroupBean mChatGroupBean;
    /**是否需要请求网络*/
    private boolean mIsNeedChange;

    public static GroupManagerFragment instance(Bundle bundle) {
        GroupManagerFragment fragment = new GroupManagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_DATA);
        if (mChatGroupBean != null){
            mScOpen.setChecked(mChatGroupBean.isMembersonly());
            mIsNeedChange = true;
        }
    }

    @Override
    protected void initData() {
        mScOpen.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (mIsNeedChange){
                    // 有所改变才去更新群信息
                    mChatGroupBean.setMembersonly(isChecked);
                    mPresenter.updateGroup(mChatGroupBean);
                    mIsNeedChange = false;
                }
        });
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_group_manager);
    }

    @OnClick({R.id.ll_change_owner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_owner:
                // 跳转群管理
                Intent intent = new Intent(getContext(), EditGroupOwnerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_GROUP_DATA, mChatGroupBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_group_manager;
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean) {
        if (chatGroupBean != null){
            // emm 由于没有完全返回所有信息 再加上字段也不同 所以手动改一下
            mChatGroupBean.setMembersonly(chatGroupBean.isMembers_only());
            mIsNeedChange = true;
            EventBus.getDefault().post(mChatGroupBean, EventBusTagConfig.EVENT_IM_GROUP_DATA_CHANGED);
        } else {
            // 失败了
            mIsNeedChange = false;
            mScOpen.setChecked(mChatGroupBean.isMembersonly());
            mIsNeedChange = true;
        }
    }
}

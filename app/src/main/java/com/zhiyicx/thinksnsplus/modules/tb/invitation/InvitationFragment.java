package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InvitationFragment extends TSFragment<InvitationContract.Presenter> implements InvitationContract.View {

    @BindView(R.id.iv_top_logo)
    ImageView mIvTopLogo;
    @BindView(R.id.tv_invitation_code)
    TextView mTvInvitationCode;
    @BindView(R.id.tv_click_copy)
    TextView mTvClickCopy;
    @BindView(R.id.iv_2code)
    ImageView mIv2code;

    /**
     * 用于分享截图的布局
     */
    @BindView(R.id.ll_share_contianer)
    LinearLayout mLlShareContianer;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_save)
    TextView mTvSave;

    public static InvitationFragment newInstance(Bundle bundle) {
        InvitationFragment invitationFragment = new InvitationFragment();
        invitationFragment.setArguments(bundle);
        return invitationFragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_invitation;
    }

    @OnClick({R.id.tv_invitation_code, R.id.tv_click_copy, R.id.tv_wx, R.id.tv_friend, R.id.tv_copy, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 邀请码 ， 点击复制
            case R.id.tv_invitation_code:

            case R.id.tv_click_copy:
                break;
            // 微信
            case R.id.tv_wx:
                break;
            // 朋友圈
            case R.id.tv_friend:
                break;
            // 复制链接
            case R.id.tv_copy:
                break;
            // 保存
            case R.id.tv_save:
                break;
            default:
        }
    }
}

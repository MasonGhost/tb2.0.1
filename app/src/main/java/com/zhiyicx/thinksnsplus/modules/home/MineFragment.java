package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.CombinationButton;
import com.zhiyicx.baseproject.widget.infohint.ShowHintInfo;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.loader.SkinManager;

/**
 * @Describe 我的页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MineFragment extends TSFragment {

    @BindView(R.id.iv_head_icon)
    ImageView mIvHeadIcon;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_user_signature)
    TextView mTvUserSignature;
    @BindView(R.id.rl_userinfo_container)
    RelativeLayout mRlUserinfoContainer;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.ll_fans_container)
    LinearLayout mLlFansContainer;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.ll_follow_container)
    LinearLayout mLlFollowContainer;
    @BindView(R.id.bt_personal_page)
    CombinationButton mBtPersonalPage;
    @BindView(R.id.bt_ranking)
    CombinationButton mBtRanking;
    @BindView(R.id.bt_gold)
    CombinationButton mBtGold;
    @BindView(R.id.bt_suggestion)
    CombinationButton mBtSuggestion;
    @BindView(R.id.bt_question_answer)
    CombinationButton mBtQuestionAnswer;
    @BindView(R.id.bt_setting)
    CombinationButton mBtSetting;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.mine);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected int setToolBarBackgroud() {
        StatusBarUtils.statusBarLightMode(getActivity());//当状态栏颜色为白色时使用，Activity 中最后一次调用确定状态栏背景颜色和图标颜色
        return R.color.white;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.rl_userinfo_container, R.id.ll_fans_container, R.id.ll_follow_container, R.id.bt_personal_page, R.id.bt_ranking, R.id.bt_gold, R.id.bt_suggestion, R.id.bt_question_answer, R.id.bt_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo_container:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.ll_fans_container:
                break;
            case R.id.ll_follow_container:
                break;
            case R.id.bt_personal_page:
                SkinManager.getInstance().restoreDefaultTheme();
                break;
            case R.id.bt_ranking:
                SkinManager.getInstance().loadSkin("tsplustheme.skin", new SkinLoaderListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        ToastUtils.showToast("加载成功");
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        ToastUtils.showToast("加载失败-->" + errMsg);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.bt_gold:
                ShowHintInfo.showSendError();
                break;
            case R.id.bt_suggestion:
                break;
            case R.id.bt_question_answer:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.bt_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
    }
}

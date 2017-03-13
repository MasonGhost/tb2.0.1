package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.loader.SkinManager;

/**
 * @Describe 我的页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MineFragment extends TSFragment<MineContract.Presenter> implements MineContract.View {

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
    @Inject
    public MinePresenter mMinePresenter;

    private UserInfoBean mUserInfoBean;

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
        DaggerMinePresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .minePresenterModule(new MinePresenterModule(this))
                .build().inject(this);
        mPresenter.getUserInfoFromDB();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.mine);
    }

    @Override
    protected int setLeftImg() {
        return 0;
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

    @OnClick({R.id.rl_userinfo_container, R.id.ll_fans_container, R.id.ll_follow_container, R.id.bt_personal_page, R.id.bt_ranking, R.id.bt_gold, R.id.bt_suggestion, R.id.bt_question_answer, R.id.bt_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo_container:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.ll_fans_container:
                Bundle bundleFans = new Bundle();
                bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
                Intent itFans = new Intent(getActivity(), FollowFansListActivity.class);
                itFans.putExtras(bundleFans);
                startActivity(itFans);
                break;
            case R.id.ll_follow_container:
                Bundle bundleFollow = new Bundle();
                bundleFollow.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
                Intent itFollow = new Intent(getActivity(), FollowFansListActivity.class);
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.bt_personal_page:
                Intent intent = new Intent(getContext(), PersonalCenterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, mUserInfoBean);
                intent.putExtras(bundle);
                startActivity(intent);
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
                startActivity(new Intent(getActivity(), GalleryActivity.class));
                break;
            case R.id.bt_suggestion:
                //LoadingDialogUtils.showStateSuccess(getContext());
                break;
            case R.id.bt_question_answer:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.bt_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            default:
        }
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return;
        }
        this.mUserInfoBean = userInfoBean;
        if (userInfoBean == null) {
            return;
        }
        // 设置用户头像
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView(mIvHeadIcon)
                .url(ImageUtils.imagePathConvert(mUserInfoBean.getAvatar(), ImageZipConfig.IMAGE_60_ZIP))
                .placeholder(R.drawable.shape_default_image_circle)
                .errorPic(R.drawable.shape_default_image_circle)
                .build());
        // 设置用户名
        mTvUserName.setText(userInfoBean.getName());
        // 设置简介
        mTvUserSignature.setText(userInfoBean.getIntro());
        // 设置粉丝数
        mTvFansCount.setText(userInfoBean.getFollowed_count());
        // 设置关注数
        mTvFollowCount.setText(userInfoBean.getFollowing_count());
    }

    @Override
    public void updateUserFollowCount(int stateFollow) {
        switch (stateFollow) {
            case FollowFansBean.IFOLLOWED_STATE:
                // 添加一个关注
                mUserInfoBean.setFollowing_count(Integer.parseInt(mUserInfoBean.getFollowing_count()) + 1 + "");
                break;
            case FollowFansBean.UNFOLLOWED_STATE:
                // 取消一个关注
                mUserInfoBean.setFollowing_count(Integer.parseInt(mUserInfoBean.getFollowing_count()) - 1 + "");
                break;
        }
        mTvFollowCount.setText(mUserInfoBean.getFollowing_count());
    }
}

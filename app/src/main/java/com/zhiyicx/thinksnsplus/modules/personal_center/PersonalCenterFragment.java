package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicCountItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem;
import com.zhiyicx.thinksnsplus.widget.NestedScrollLineayLayout;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 用户个人中心页面
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterFragment extends TSListFragment<PersonalCenterContract.Presenter, DynamicBean> implements PersonalCenterContract.View, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener {

    public static final String PERSONAL_CENTER_DATA = "personal_center_data";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.ll_follow_container)
    LinearLayout mLlFollowContainer;
    @BindView(R.id.ll_chat_container)
    LinearLayout mLlChatContainer;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;
    @BindView(R.id.nest_scroll_container)
    NestedScrollLineayLayout mNestScrollContainer;
    @BindView(R.id.personal_header)
    LinearLayout mPersonalHeader;


    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private PersonalCenterHeaderViewItem mPersonalCenterHeaderViewItem;
    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    // 关注状态
    private FollowFansBean mFollowFansBean;
    // 上一个页面传过来的用户信息
    private UserInfoBean mUserInfoBean;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolBar();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

       // mNestScrollContainer.setHeaderViewId(R.id.personal_header);
        mNestScrollContainer.setNotConsumeHeight(200);
        mNestScrollContainer.setOnHeadFlingListener(new NestedScrollLineayLayout.OnHeadFlingListener() {
            @Override
            public void onHeadFling(int scrollY) {
                int distance = mNestScrollContainer.getTopViewHeight();
                int alpha = 255 * scrollY / distance;
                alpha = alpha > 255 ? 255 : alpha;
                mRlToolbarContainer.getBackground().setAlpha(alpha);
            }
        });
        mPersonalCenterHeaderViewItem = new PersonalCenterHeaderViewItem(getActivity(), mRvList, mPersonalHeader);
        mPersonalCenterHeaderViewItem.initHeaderView();
        // 添加关注点击事件
        RxView.clicks(mTvFollow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 表示第一次进入界面加载正确的关注状态，后续才能进行关注操作
                        if (mFollowFansBean != null) {
                            mPresenter.handleFollow(mFollowFansBean);
                        }
                    }
                });

    }


    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mUserInfoBean.getUser_id());
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {

    }

    @Override
    protected boolean getPullDownRefreshEnable() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        adapter.addItemViewDelegate(new PersonalCenterDynamicCountItem());
        setAdapter(adapter, new DynamicListBaseItem(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        return adapter;
    }

    @Override
    protected void initData() {
        mUserInfoBean = getArguments().getParcelable(PERSONAL_CENTER_DATA);
        // 进入页面尝试设置头部信息
        setHeaderInfo(mUserInfoBean);
        // 获取个人主页用户信息，显示在headerView中
        mPresenter.setCurrentUserInfo(mUserInfoBean.getUser_id());
        // 获取动态列表
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false, mUserInfoBean.getUser_id());
        // 获取关注状态
        mPresenter.initFollowState(mUserInfoBean.getUser_id());
    }

    @Override
    public void setPresenter(PersonalCenterContract.Presenter presenter) {
        this.mPresenter = presenter;
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
    public void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position) {

    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {

    }

    @Override
    public void onReSendClick(int position) {

    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_more:
                break;

        }
    }

    @Override
    public void setHeaderInfo(UserInfoBean userInfoBean) {
        setBottomVisible(userInfoBean.getUser_id());
        mPersonalCenterHeaderViewItem.initHeaderViewData(userInfoBean);
    }

    @Override
    public void setFollowState(FollowFansBean followFansBean) {
        mFollowFansBean = followFansBean;
        setBottomFollowState(followFansBean.getFollowState());
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, DeviceUtils.getStatuBarHeight(getContext()), 0, 0);
        mRlToolbarContainer.setLayoutParams(layoutParams);
    }

    /**
     * 设置底部view的关注状态
     */
    private void setBottomFollowState(int state) {
        switch (state) {
            case FollowFansBean.UNFOLLOWED_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_follow), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
                mTvFollow.setText(R.string.follow);
                break;
            case FollowFansBean.IFOLLOWED_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_followed), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mTvFollow.setText(R.string.followed);
                break;
            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_followed_eachother), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mTvFollow.setText(R.string.followed_eachother);
                break;
            default:
        }
    }

    /**
     * 设置底部view的可见性;如果进入了当前登录用户的主页，需要隐藏底部状态栏
     *
     * @param currentUserID
     */
    private void setBottomVisible(long currentUserID) {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        mLlBottomContainer.setVisibility(authBean.getUser_id() == currentUserID ? View.GONE : View.VISIBLE);
    }

    public static PersonalCenterFragment initFragment(Bundle bundle) {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        personalCenterFragment.setArguments(bundle);
        return personalCenterFragment;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }

    /**
     * 跳转到当前的个人中心页面
     */
    public static void startToPersonalCenter(Context context, UserInfoBean userInfoBean) {
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, userInfoBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}

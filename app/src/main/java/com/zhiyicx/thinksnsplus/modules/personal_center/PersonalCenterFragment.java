package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBoundTransform;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.ZoomView;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForDig;
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
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiuChao
 * @describe 用户个人中心页面
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterFragment extends TSListFragment<PersonalCenterContract.Presenter, DynamicBean> implements PersonalCenterContract.View, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, DynamicListBaseItem.OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener {

    public static final String PERSONAL_CENTER_DATA = "personal_center_data";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private PersonalCenterHeaderViewItem mPersonalCenterHeaderViewItem;
    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    // 当前需要显示的用户的id
    private long currentUserId = 5;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolBar();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mPersonalCenterHeaderViewItem = new PersonalCenterHeaderViewItem(getActivity(), mRvList, mHeaderAndFooterWrapper);
        mPersonalCenterHeaderViewItem.initHeaderView();
    }


    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, currentUserId);
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
        // 获取个人主页用户信息，显示在headerView中
        mPresenter.setCurrentUserInfo(currentUserId);
        // 获取动态列表
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false, currentUserId);
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
        mPersonalCenterHeaderViewItem.initHeaderViewData(userInfoBean);
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, DeviceUtils.getStatuBarHeight(getContext()), 0, 0);
        mRlToolbarContainer.setLayoutParams(layoutParams);
    }
}

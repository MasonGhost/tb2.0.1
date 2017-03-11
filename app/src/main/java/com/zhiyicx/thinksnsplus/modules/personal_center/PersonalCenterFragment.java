package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 用户个人中心页面
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterFragment extends TSListFragment<PersonalCenterContract.Presenter, DynamicBean> implements PersonalCenterContract.View, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener, PhotoSelectorImpl.IPhotoBackListener {

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
    @BindView(R.id.ll_toolbar_container_parent)
    LinearLayout mLlToolbarContainerParent;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private PersonalCenterHeaderViewItem mPersonalCenterHeaderViewItem;
    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    // 关注状态
    private FollowFansBean mFollowFansBean;
    // 上一个页面传过来的用户信息
    private UserInfoBean mUserInfoBean;
    private PhotoSelectorImpl mPhotoSelector;
    private String imagePath;// 上传的封面图片的本地路径

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        // 初始化图片选择器
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_RCTANGLE))
                .build().photoSelectorImpl();
        initToolBar();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mPersonalCenterHeaderViewItem = new PersonalCenterHeaderViewItem(getActivity(), mPhotoSelector, mRvList, mHeaderAndFooterWrapper, mLlToolbarContainerParent);
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
    protected List<DynamicBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, mUserInfoBean.getUser_id());
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
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
        //adapter.addItemViewDelegate(new PersonalCenterDynamicCountItem());
        setAdapter(adapter, new PersonalCenterDynamicListBaseItem(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForNineImage(getContext()));
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
        if (userInfoBean != null) {
            setBottomVisible(userInfoBean.getUser_id());
            mPersonalCenterHeaderViewItem.initHeaderViewData(userInfoBean);
        }
    }

    @Override
    public void setFollowState(FollowFansBean followFansBean) {
        mFollowFansBean = followFansBean;
        setBottomFollowState(followFansBean.getFollowState());
    }

    @Override
    public void setUpLoadCoverState(boolean upLoadState, int taskId) {
        if (upLoadState) {
            // 封面图片上传成功
            ToastUtils.showToast("封面上传成功");
            // 通知服务器，更改用户信息
            mPresenter.changeUserCover(mUserInfoBean, taskId, imagePath);
        } else {
            ToastUtils.showToast("封面上传失败");
        }
    }

    @Override
    public void setChangeUserCoverState(boolean changeSuccess) {
        ToastUtils.showToast(changeSuccess ? "封面修改成功" : "封面修改失败");
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 选择图片完毕后，开始上传封面图片
        ImageBean imageBean = photoList.get(0);
        imagePath = imageBean.getImgUrl();
        // 加载本地图片
        mPresenter.uploadUserCover(imagePath);
        // 上传本地图片
        mPersonalCenterHeaderViewItem.upDateUserCover(imagePath);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height) + DeviceUtils.getStatuBarHeight(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        //layoutParams.setMargins(0, DeviceUtils.getStatuBarHeight(getContext()), 0, 0);
        mLlToolbarContainerParent.setLayoutParams(layoutParams);
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

}

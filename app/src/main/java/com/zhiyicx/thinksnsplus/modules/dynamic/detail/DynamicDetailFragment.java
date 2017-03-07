package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForContent;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForDig;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_3;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailFragment extends TSListFragment<DynamicDetailContract.Presenter, DynamicBean> implements DynamicDetailContract.View {
    public static final String DYNAMIC_DETAIL_DATA = "dynamic_detail_data";
    // 动态详情列表，各个item的位置
    private static final int DYNAMIC_ITEM_CONTENT = 0;
    private static final int DYNAMIC_ITEM_DIG = 1;
    //private static final int DYNAMIC_ITEM_COMMENT >1;

    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private DynamicBean mDynamicBean;// 上一个页面传进来的数据
    private FollowFansBean mFollowFansBean;// 用户关注状态
    private List<DynamicBean> mDatas = new ArrayList<>();

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initBottomToolUI();
        initBottomToolListener();

    }

    @Override
    protected int setRightImg() {
        return R.mipmap.detail_ico_follow;
    }

    @Override
    protected void initData() {
        // 处理上个页面传过来的动态数据
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DYNAMIC_DETAIL_DATA)) {
            DynamicBean dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            mDynamicBean = dynamicBean;
            setToolBarUser(dynamicBean);// 设置标题用户
            initBottomToolData(dynamicBean);// 初始化底部工具栏数据
            // 设置动态详情列表数据
            DynamicBean dynamicContent = new DynamicBean();// 设置详情内容
            dynamicContent.setFeed(dynamicBean.getFeed());
            DynamicBean dynamicDig = new DynamicBean();// 设置点赞
            dynamicDig.setFeed(dynamicBean.getFeed());
            dynamicDig.setTool(dynamicBean.getTool());
            mDatas.add(dynamicContent);
            mDatas.add(dynamicDig);
            refreshData();
        }
    }

    //不显示分割线
    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        MultiItemTypeAdapter<DynamicBean> adapter = new MultiItemTypeAdapter<>(getContext(), mDatas);
        adapter.addItemViewDelegate(new DynamicDetailItemForContent());
        adapter.addItemViewDelegate(new DynamicDetailItemForDig());
        return adapter;
    }

    @Override
    public void setPresenter(DynamicDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void setRightClick() {
        if (mFollowFansBean != null) {
            mPresenter.handleFollowUser(mFollowFansBean);
        }
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

    public static DynamicDetailFragment initFragment(Bundle bundle) {
        DynamicDetailFragment dynamicDetailFragment = new DynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

    /**
     * 设置toolBar上面的用户头像,关注状态
     */
    private void setToolBarUser(DynamicBean dynamicBean) {
        // 设置用户头像，名称
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        UserInfoBean userInfoBean = dynamicBean.getUserInfoBean();// 动态所属用户的信息
        mTvToolbarCenter.setText(userInfoBean.getName());
        int headIconWidth = getResources().getDimensionPixelSize(R.dimen.headpic_for_assist);
        Glide.with(getContext())
                .load(String.format(ApiConfig.IMAGE_PATH, 20, 10))
                .bitmapTransform(new GlideCircleTransform(getContext()))
                .override(headIconWidth, headIconWidth)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        resource.setBounds(0, 0, resource.getMinimumWidth(), resource.getMinimumHeight());
                        mTvToolbarCenter.setCompoundDrawables(resource, null, null, null);
                    }
                });
        // 如果当前动态所属用户，就是当前用户，隐藏关注按钮
        long user_id = dynamicBean.getUser_id();
        if (user_id == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            mTvToolbarRight.setVisibility(View.GONE);
        } else {
            // 获取用户关注状态
            mPresenter.getUserFollowState(user_id + "");
            mTvToolbarRight.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setLike(boolean isLike) {
        mDdDynamicTool.setItemIsChecked(isLike, ITEM_POSITION_0);
    }

    @Override
    public void setCollect(boolean isCollect) {
        mDdDynamicTool.setItemIsChecked(isCollect, ITEM_POSITION_3);
    }

    @Override
    public void setDigHeadIcon(List<FollowFansBean> userInfoBeanList) {
        DynamicBean dynamicBean = mDatas.get(DYNAMIC_ITEM_DIG);
        dynamicBean.setDigUserInfoList(userInfoBeanList);
        refreshData(DYNAMIC_ITEM_DIG);
    }

    @Override
    public void upDateFollowFansState(int followState) {
        setToolBarRightFollowState(followState);
    }

    @Override
    public void initFollowState(FollowFansBean mFollowFansBean) {
        this.mFollowFansBean = mFollowFansBean;
        setToolBarRightFollowState(mFollowFansBean.getFollowState());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, getContext());
    }

    /**
     * 设置底部工具栏UI
     */
    private void initBottomToolUI() {
        // 初始化底部工具栏数据
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.home_ico_good_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.detail_ico_good_uncollect
        });
        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.home_ico_good_high
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.detail_ico_collect
        });
        mDdDynamicTool.setButtonText(new int[]{R.string.dynamic_like, R.string.comment
                , R.string.share, R.string.favorite});

    }

    /**
     * 进入页面，设置底部工具栏的数据
     */
    private void initBottomToolData(DynamicBean dynamicBean) {
        DynamicToolBean dynamicToolBean = dynamicBean.getTool();
        // 设置是否喜欢
        mDdDynamicTool.setItemIsChecked(dynamicToolBean.getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED, DynamicDetailMenuView.ITEM_POSITION_0);
        //设置是否收藏
        mDdDynamicTool.setItemIsChecked(dynamicToolBean.getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED, DynamicDetailMenuView.ITEM_POSITION_3);
    }

    /**
     * 设置底部工具栏的点击事件
     */
    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick(new DynamicDetailMenuView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int postion) {
                mDdDynamicTool.getTag(R.id.view_data);
                switch (postion) {
                    case DynamicDetailMenuView.ITEM_POSITION_0:
                        // 喜欢
                        // 修改数据
                        DynamicToolBean likeToolBean = mDynamicBean.getTool();
                        likeToolBean.setIs_digg_feed(likeToolBean.getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED
                                ? DynamicToolBean.STATUS_DIGG_FEED_CHECKED : DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED);
                        // 处理喜欢逻辑，包括服务器，数据库，ui
                        mPresenter.handleLike(mDynamicBean.getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED,
                                mDynamicBean.getFeed_id(), likeToolBean);
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_1:
                        // 评论

                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_2:
                        // 分享
                        mPresenter.shareDynamic();
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_3:
                        // 收藏
                        // 修改数据
                        DynamicToolBean collectToolBean = mDynamicBean.getTool();
                        collectToolBean.setIs_collection_feed(collectToolBean.getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED
                                ? DynamicToolBean.STATUS_COLLECT_FEED_CHECKED : DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED);
                        // 处理喜欢逻辑，包括服务器，数据库，ui
                        mPresenter.handleCollect(mDynamicBean.getTool().getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED,
                                mDynamicBean.getFeed_id(), collectToolBean);
                        break;
                }
            }
        });
    }

    /**
     * 设置toolBar上面的关注状态
     */
    private void setToolBarRightFollowState(int state) {
        mToolbarRight.setVisibility(View.VISIBLE);
        switch (state) {
            case FollowFansBean.UNFOLLOWED_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_follow), null);
                break;
            case FollowFansBean.IFOLLOWED_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed), null);
                break;
            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed_eachother), null);
                break;
            default:
        }
    }

}

package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
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
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForDig;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
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

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<DynamicBean> mDynamicBeens = new ArrayList<>();

    /**********************************
     * headerView控件
     ********************************/
    private FrameLayout fl_cover_contaner;// 封面图的容器
    private ImageView iv_background_cover;// 封面
    private ImageView iv_head_icon;// 用户头像
    private TextView tv_user_name;// 用户名
    private TextView tv_user_intro;// 用户简介
    private TextView tv_user_follow;// 用户关注数量
    private TextView tv_user_fans;// 用户粉丝数量


    @Override
    protected void initView(View rootView) {

        super.initView(rootView);
        initToolBar();
        initHeaderView();
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
        mDynamicBeens.add(new DynamicBean());
        mDynamicBeens.add(new DynamicBean());
        mDynamicBeens.add(new DynamicBean());
        mDynamicBeens.add(new DynamicBean());
        mDynamicBeens.add(new DynamicBean());

        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        adapter.addItemViewDelegate(new DynamicDetailItemForDig());
      /*  setAdapter(adapter, new DynamicListBaseItem(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));*/
        return adapter;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(PersonalCenterContract.Presenter presenter) {

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
    public void onUserInfoClick(UserInfoBean userInfoBean) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_personal_center_header, null);
        initHeaderViewUI(headerView);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initHeaderViewUI(View headerView) {
        ViewGroup.LayoutParams headerLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(headerLayoutParams);
        fl_cover_contaner = (FrameLayout) headerView.findViewById(R.id.fl_cover_contaner);
        iv_background_cover = (ImageView) headerView.findViewById(R.id.iv_background_cover);
        // 高度为屏幕宽度一半加上20dp
        int height = UIUtils.getWindowWidth(getContext()) / 2 + getResources().getDimensionPixelSize(R.dimen.spacing_large);
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        fl_cover_contaner.setLayoutParams(containerLayoutParams);
        initHeaderZoom();
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, DeviceUtils.getStatuBarHeight(getContext()), 0, 0);
        mRlToolbarContainer.setLayoutParams(layoutParams);
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


    /**
     * 图片缩放的处理
     */
    private void initzoomimage() {

        mRvList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fl_cover_contaner.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            //当图片也就是第一个item完全可见的时候，记录触摸屏幕的位置
                            if (((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                        if (distance < 0) {
                            break;
                        }
                        // 处理放大
                        mScaling = true;
                        lp.width = metric.widthPixels + distance;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        fl_cover_contaner.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });

    }

    private DisplayMetrics metric;
    // 是否正在放大
    private Boolean mScaling = false;
    // 记录首次按下位置
    private float mFirstPosition = 0;

    private void replyImage() {
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fl_cover_contaner.getLayoutParams();
        final float w = fl_cover_contaner.getLayoutParams().width;// 图片当前宽度
        final float h = fl_cover_contaner.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                fl_cover_contaner.setLayoutParams(lp);
            }
        });
        anim.start();

    }

    private void initHeaderZoom() {
        metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fl_cover_contaner.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        fl_cover_contaner.setLayoutParams(lp);
        initzoomimage();
    }

}

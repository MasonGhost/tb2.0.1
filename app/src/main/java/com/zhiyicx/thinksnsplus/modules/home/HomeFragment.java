package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.jpush.JpushAlias;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.FindFragment;
import com.zhiyicx.thinksnsplus.modules.home.main.MainFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.MineFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.zhiyicx.common.utils.StatusBarUtils.STATUS_TYPE_ANDROID_M;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */
public class HomeFragment extends TSFragment<HomeContract.Presenter> implements DynamicFragment.OnCommentClickListener, HomeContract.View, PhotoSelectorImpl.IPhotoBackListener {
    public static final int PAGE_NUMS = 4; // 页数

    public static final int PAGE_HOME = 0; // 对应在 viewpager 中的位置
    public static final int PAGE_FIND = 1;
    public static final int PAGE_MESSAGE = 2;
    public static final int PAGE_MINE = 3;

    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.iv_find)
    ImageView mIvFind;
    @BindView(R.id.tv_find)
    TextView mTvFind;
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.v_message_tip)
    View mVMessageTip;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.iv_mine)
    ImageView mIvMine;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.vp_home)
    NoPullViewPager mVpHome;

    @Inject
    HomePresenter mHomePresenter;  // 仅用于构造
    @BindView(R.id.fl_add)
    FrameLayout mFlAdd;
    @BindView(R.id.ll_message)
    LinearLayout mLlMessage;
    @BindView(R.id.ll_mine)
    LinearLayout mLlMine;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;
    private TSViewPagerAdapter mHomePager;
    private PhotoSelectorImpl mPhotoSelector;
    private JpushAlias mJpushAlias;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 不需要 toolbar
     *
     * @return
     */
    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        initViewPager();
        longClickSendTextDynamic();
        initPhotoPicker();
    }


    @Override
    protected void initData() {
        DaggerHomeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .homePresenterModule(new HomePresenterModule(this))
                .build()
                .inject(this);
        initListener();
        changeNavigationButton(PAGE_HOME);
        mVpHome.setCurrentItem(PAGE_HOME, false);
        mJpushAlias = new JpushAlias(getContext(), AppApplication.getmCurrentLoginAuth().getUser_id() + "");// 设置极光推送别名
        mJpushAlias.setAlias();

        Glide.with(getActivity()).load("");
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home;
    }

    @OnClick({R.id.ll_home, R.id.ll_find, R.id.fl_add, R.id.ll_message, R.id.ll_mine})
    public void onClick(final View view) {
        switch (view.getId()) {
            // 点击主页
            case R.id.ll_home:
                mVpHome.setCurrentItem(PAGE_HOME, false);
                break;
            // 点击发现
            case R.id.ll_find:
                mVpHome.setCurrentItem(PAGE_FIND, false);
                break;
            // 添加动态
            case R.id.fl_add:
                clickSendPhotoTextDynamic();
                break;
            // 点击消息
            case R.id.ll_message:
                mVpHome.setCurrentItem(PAGE_MESSAGE, false);
                break;
            // 点击我的
            case R.id.ll_mine:
                mVpHome.setCurrentItem(PAGE_MINE, false);
                break;
            default:
        }

    }

    /**
     * 初始化 viewpager
     */

    private void initViewPager() {
        //设置缓存的个数
        mVpHome.setOffscreenPageLimit(PAGE_NUMS);
        mHomePager = new TSViewPagerAdapter(getActivity().getSupportFragmentManager());
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(MainFragment.newInstance(this));
        mFragmentList.add(FindFragment.newInstance());
        mFragmentList.add(MessageFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());
        mHomePager.bindData(mFragmentList);//将List设置给adapter
        mVpHome.setAdapter(mHomePager);
    }

    /**
     * 设置监听
     */
    private void initListener() {
        //设置滚动监听
        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeNavigationButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 设置 IM 监听
        mPresenter.initIM();
    }

    /**
     * 改变导航栏按钮的状态
     *
     * @param position 当前 viewpager 的位置
     */
    private void changeNavigationButton(int position) {
        int checkedColor = ContextCompat.getColor(getContext(), R.color.themeColor);
        int unckeckedColor = ContextCompat.getColor(getContext(), R.color.home_bottom_navigate_text_normal);
        mIvHome.setImageResource(position == PAGE_HOME ? R.mipmap.common_ico_bottom_home_high : R.mipmap.common_ico_bottom_home_normal);
        mTvHome.setTextColor(position == PAGE_HOME ? checkedColor : unckeckedColor);
        mIvFind.setImageResource(position == PAGE_FIND ? R.mipmap.common_ico_bottom_discover_high : R.mipmap.common_ico_bottom_discover_normal);
        mTvFind.setTextColor(position == PAGE_FIND ? checkedColor : unckeckedColor);
        mIvMessage.setImageResource(position == PAGE_MESSAGE ? R.mipmap.common_ico_bottom_message_high : R.mipmap.common_ico_bottom_message_normal);
        mTvMessage.setTextColor(position == PAGE_MESSAGE ? checkedColor : unckeckedColor);
        mIvMine.setImageResource(position == PAGE_MINE ? R.mipmap.common_ico_bottom_me_high : R.mipmap.common_ico_bottom_me_normal);
        mTvMine.setTextColor(position == PAGE_MINE ? checkedColor : unckeckedColor);
    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
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
    public void setMessageTipVisable(boolean tipVisable) {
        if (tipVisable) {
            mVMessageTip.setVisibility(View.VISIBLE);
        } else {
            mVMessageTip.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 长按动态发送按钮，进入纯文字的动态发布
     */
    private void longClickSendTextDynamic() {
        mFlAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent it = new Intent(getContext(), SendDynamicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(SendDynamicActivity.DYNAMIC_TYPE, SendDynamicActivity.TEXT_ONLY_DYNAMIC);
                it.putExtras(bundle);
                startActivity(it);
                return true;
            }
        });
    }

    /**
     * 点击动态发送按钮，进入文字图片的动态发布
     */
    private void clickSendPhotoTextDynamic() {
        mPhotoSelector.getPhotoListFromSelector(9, null);
    }

    private void initPhotoPicker() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 跳转到发送动态页面
        Intent it = new Intent(getContext(), SendDynamicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SendDynamicActivity.DYNAMIC_PHOTOS, (ArrayList<? extends Parcelable>) photoList);
        bundle.putInt(SendDynamicActivity.DYNAMIC_TYPE, SendDynamicActivity.PHOTO_TEXT_DYNAMIC);
        it.putExtras(bundle);
        startActivity(it);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onButtonMenuShow(boolean isShow) {
        if (isShow) {
            Observable.timer(getResources().getInteger(android.R.integer.config_longAnimTime), TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, Object>() {
                @Override
                public Object call(Long aLong) {
                    mLlBottomContainer.setVisibility(View.VISIBLE);
                    return null;
                }
            }).subscribe();
        } else {
            mLlBottomContainer.setVisibility(View.GONE);
        }

    }

}

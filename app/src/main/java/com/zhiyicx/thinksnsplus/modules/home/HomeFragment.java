package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.appupdate.AppUpdateManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.jpush.JpushAlias;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.FindFragment;
import com.zhiyicx.thinksnsplus.modules.home.main.MainFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.MineFragment;
import com.zhiyicx.thinksnsplus.widget.popwindow.CheckInPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.MAX_DEFAULT_COUNT;
import static com.zhiyicx.thinksnsplus.modules.home.HomeActivity.BUNDLE_JPUSH_MESSAGE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */
public class HomeFragment extends TSFragment<HomeContract.Presenter> implements DynamicFragment.OnCommentClickListener, HomeContract.View,
        PhotoSelectorImpl.IPhotoBackListener {
    /**
     * 页数
     */
    public static final int PAGE_NUMS = 4;

    /**
     * 对应在 viewpager 中的位置
     */
    public static final int PAGE_HOME = 0;
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
    @BindView(R.id.v_mine_tip)
    View mVMineTip;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.vp_home)
    NoPullViewPager mVpHome;

    /**
     * 仅用于构造
     */
    @Inject
    HomePresenter mHomePresenter;

    @BindView(R.id.fl_add)
    FrameLayout mFlAdd;

    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;


    private PhotoSelectorImpl mPhotoSelector;

    private int mCurrenPage;

    /**
     * 图片选择弹框
     */
    private ActionPopupWindow mPhotoPopupWindow;
    /**
     * 签到弹窗
     */
    private CheckInPopWindow mCheckInPopWindow;

    /**
     * 签到信息
     */
    private CheckInBean mCheckInBean;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean usePermisson() {
        return true;
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
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        DaggerHomeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .homePresenterModule(new HomePresenterModule(this))
                .build()
                .inject(this);
        initViewPager();
        longClickSendTextDynamic();
        initPhotoPicker();
        initListener();
    }


    @Override
    protected void initData() {
        setJpushAlias();
        changeNavigationButton(PAGE_HOME);
        setCurrentPage();
        // app更新
        AppUpdateManager.getInstance(getContext()
                , ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_GET_APP_VERSION + "?version_code=" + DeviceUtils.getVersionCode(getContext()) +
                        "&type=android")
                .startVersionCheck();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 游客登录后的处理
        if (mPresenter.isLogin() && mFragmentList.size() < PAGE_NUMS) {
            initViewPager();
            mVpHome.setCurrentItem(mCurrenPage, false);
        }


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
                if (mCurrenPage == PAGE_HOME) {
                    ((MainFragment) mFragmentList.get(mCurrenPage)).refreshCurrentPage();
                } else {
                    mVpHome.setCurrentItem(PAGE_HOME, false);
                    mCurrenPage = PAGE_HOME;
                }
                break;
            // 点击发现
            case R.id.ll_find:
                if (TouristConfig.FIND_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_FIND, false);
                }
                mCurrenPage = PAGE_FIND;
                break;
            // 添加动态
            case R.id.fl_add:
                if (TouristConfig.DYNAMIC_CAN_PUBLISH || !mPresenter.handleTouristControl()) {
                    Intent intent = new Intent(getActivity(), SelectDynamicTypeActivity.class);
                    startActivity(intent);
                }
                break;
            // 点击消息
            case R.id.ll_message:
                if (TouristConfig.MESSAGE_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_MESSAGE, false);
                }
                mCurrenPage = PAGE_MESSAGE;
                break;
            // 点击我的
            case R.id.ll_mine:
                if (TouristConfig.MINE_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_MINE, false);
                }
                mCurrenPage = PAGE_MINE;
                break;
            default:
        }

    }

    @Override
    public void setMessageTipVisable(boolean tipVisable) {
        if (tipVisable) {
            mVMessageTip.setVisibility(View.VISIBLE);
        } else {
            mVMessageTip.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void setMineTipVisable(boolean tipVisable) {
        if (tipVisable) {
            mVMineTip.setVisibility(View.VISIBLE);
        } else {
            mVMineTip.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void checkBottomItem(int positon) {
        mVpHome.setCurrentItem(positon, false);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 跳转到发送动态页面
        SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
        sendDynamicDataBean.setDynamicPrePhotos(photoList);
        sendDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onButtonMenuShow(boolean isShow) {
        if (isShow) {
            Observable.timer(getResources().getInteger(android.R.integer.config_longAnimTime), TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread
                    ()).map(aLong -> {
                mLlBottomContainer.setVisibility(View.VISIBLE);
                return null;
            }).subscribe();
        } else {
            mLlBottomContainer.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化 viewpager
     */
    private void initViewPager() {
        //设置缓存的个数
        mVpHome.setOffscreenPageLimit(PAGE_NUMS - 1);
        TSViewPagerAdapter homePager = new TSViewPagerAdapter(getChildFragmentManager());

        mFragmentList.clear();
        mFragmentList.add(MainFragment.newInstance(this));
        mFragmentList.add(FindFragment.newInstance());
        if (TouristConfig.MESSAGE_CAN_LOOK || mPresenter.isLogin()) {
            mFragmentList.add(MessageContainerFragment.instance());
        }
        if (TouristConfig.MINE_CAN_LOOK || mPresenter.isLogin()) {
            mFragmentList.add(MineFragment.newInstance());
        }
        //将 List 设置给 adapter
        homePager.bindData(mFragmentList);
        mVpHome.setAdapter(homePager);

    }

    /**
     * viewpager切换的公开方法
     */
    public void setPagerSelection(int position) {
        mVpHome.setCurrentItem(position);
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

    /**
     * 设置当前页
     */
    private void setCurrentPage() {
        if (getArguments() != null && getArguments().getParcelable(BUNDLE_JPUSH_MESSAGE) != null) {
            switch (((JpushMessageBean) getArguments().getParcelable(BUNDLE_JPUSH_MESSAGE)).getType()) {
                case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_SYSTEM:
                    checkBottomItem(HomeFragment.PAGE_MINE);
                    break;
                default:
                    checkBottomItem(HomeFragment.PAGE_MESSAGE);
            }
        } else {
            mVpHome.setCurrentItem(PAGE_HOME, false);
        }
    }

    /**
     * 设置极光推送别名
     */
    private void setJpushAlias() {
        if (mPresenter.isLogin()) {
            // 设置极光推送别名
            JpushAlias jpushAlias = new JpushAlias(getContext(), AppApplication.getmCurrentLoginAuth().getUser_id() + "");
            jpushAlias.setAlias();
        }

    }

    /**
     * 长按动态发送按钮，进入纯文字的动态发布
     */
    private void longClickSendTextDynamic() {
        mFlAdd.setOnLongClickListener(v -> {
            // 跳转到发送动态页面
            if (BuildConfig.USE_TOLL) {
                return true;
            }
            SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
            sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
            sendDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
            SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
            return true;
        });
    }

    /**
     * 点击动态发送按钮，进入文字图片的动态发布
     */
    private void clickSendPhotoTextDynamic() {
        mPhotoSelector.getPhotoListFromSelector(MAX_DEFAULT_COUNT, null);
    }

    private void initPhotoPicker() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }


    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            mPhotoPopupWindow.show();
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    clickSendPhotoTextDynamic();
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
        mPhotoPopupWindow.show();
    }

    @Override
    public CheckInBean getCheckInData() {
        return mCheckInBean;
    }

    @Override
    public void showCheckInPop(CheckInBean data) {

        this.mCheckInBean = data;
        if (mCheckInPopWindow != null) {
            if (mCheckInPopWindow.isShowing()) {
                mCheckInPopWindow.setData(mCheckInBean, mPresenter.getWalletRatio(), mPresenter.getGoldName());
            } else {
                mCheckInPopWindow.setData(mCheckInBean, mPresenter.getWalletRatio(), mPresenter.getGoldName());
                mCheckInPopWindow.show();
            }
        } else {
            mCheckInPopWindow = new CheckInPopWindow(getContentView(), data, mPresenter.getGoldName(), mPresenter.getWalletRatio(), () ->
                    mPresenter.checkIn());
            mCheckInPopWindow.show();
        }
    }

}

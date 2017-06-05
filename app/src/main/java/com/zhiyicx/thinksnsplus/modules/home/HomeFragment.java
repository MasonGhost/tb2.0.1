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

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.jpush.JpushAlias;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeActivity;
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

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.MAX_DEFAULT_COUNT;
import static com.zhiyicx.thinksnsplus.modules.home.HomeActivity.BUNDLE_JPUSH_MESSAGE;

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
    @BindView(R.id.v_mine_tip)
    View mVMineTip;
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

    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框


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
                if (TouristConfig.FIND_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_FIND, false);
                }
                break;
            // 添加动态
            case R.id.fl_add:
                if (TouristConfig.DYNAMIC_CAN_PUBLISH || !mPresenter.handleTouristControl()) {
                    if (BuildConfig.USE_TOLL) {
                        Intent intent = new Intent(getActivity(), SelectDynamicTypeActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.zoom_in, 0);
                        //getActivity().overridePendingTransition(R.anim.slide_in_bottom, 0);
                    } else {
                        initPhotoPopupWindow();
                    }
                }
                break;
            // 点击消息
            case R.id.ll_message:
                if (TouristConfig.MESSAGE_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_MESSAGE, false);
                }
                break;
            // 点击我的
            case R.id.ll_mine:
                if (TouristConfig.MINE_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    mVpHome.setCurrentItem(PAGE_MINE, false);
                }
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
        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
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

    /**
     * 初始化 viewpager
     */

    private void initViewPager() {
        //设置缓存的个数
        mVpHome.setOffscreenPageLimit(PAGE_NUMS);
        mHomePager = new TSViewPagerAdapter(getChildFragmentManager());
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(MainFragment.newInstance(this));
        mFragmentList.add(FindFragment.newInstance());
        if (TouristConfig.MESSAGE_CAN_LOOK || mPresenter.isLogin()) {
            mFragmentList.add(MessageFragment.newInstance());
        }
        if (TouristConfig.MINE_CAN_LOOK || mPresenter.isLogin()) {
            mFragmentList.add(MineFragment.newInstance());
        }
        mHomePager.bindData(mFragmentList);//将 List 设置给 adapter
        mVpHome.setAdapter(mHomePager);
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
            mJpushAlias = new JpushAlias(getContext(), AppApplication.getmCurrentLoginAuth().getUser_id() + "");// 设置极光推送别名
            mJpushAlias.setAlias();
        }

    }

    /**
     * 长按动态发送按钮，进入纯文字的动态发布
     */
    private void longClickSendTextDynamic() {
        mFlAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 跳转到发送动态页面
                if (BuildConfig.USE_TOLL) {
                    return true;
                }
                SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
                sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
                sendDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
                return true;
            }
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
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        clickSendPhotoTextDynamic();
                        mPhotoPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        // 选择相机，拍照
                        mPhotoSelector.getPhotoFromCamera(null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPhotoPopupWindow.hide();
                    }
                }).build();
        mPhotoPopupWindow.show();
    }

}

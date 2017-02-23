<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.settings;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.AboutUsActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class SettingsFragment extends TSFragment<SettingsContract.Presenter> implements SettingsContract.View {

    @BindView(R.id.bt_login_out)
    CombinationButton mBtLoginOut;
    @BindView(R.id.bt_set_vertify)
    CombinationButton mBtSetVertify;
    @BindView(R.id.bt_change_password)
    CombinationButton mBtChangePassword;
    @BindView(R.id.bt_clean_cache)
    CombinationButton mBtCleanCache;
    @BindView(R.id.bt_about_us)
    CombinationButton mBtAboutUs;

    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框
    private ActionPopupWindow mLoginOutPopupWindow;// 退出登录选择弹框

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
        mPresenter.getDirCacheSize();// 获取缓存大小
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
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
        ToastUtils.showToast(message);
    }

    @Override
    public void setCacheDirSize(String size) {
        mBtCleanCache.setRightText(size);
    }

    private void initListener() {
        // 认证
        RxView.clicks(mBtSetVertify)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showMessage("vertify");
                    }
                });
        // 修改密码
        RxView.clicks(mBtChangePassword)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    }
                });
        // 清理缓存
        RxView.clicks(mBtCleanCache)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initCleanCachePopupWindow();
                        mCleanCachePopupWindow.show();
                    }
                });
        // 关于我们
        RxView.clicks(mBtAboutUs)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    }
                });
        // 退出登录
        RxView.clicks(mBtLoginOut)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initLoginOutPopupWindow();
                        mLoginOutPopupWindow.show();
                    }
                });
    }


    /**
     * 初始化清理缓存选择弹框
     */
    private void initCleanCachePopupWindow() {
        if (mCleanCachePopupWindow != null) {
            return;
        }
        mCleanCachePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.clean_cache))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mPresenter.cleanCache();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mCleanCachePopupWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化清理缓存选择弹框
     */
    private void initLoginOutPopupWindow() {
        if (mLoginOutPopupWindow != null) {
            return;
        }
        mLoginOutPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.login_out))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        if(mPresenter.loginOut()){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        mLoginOutPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mLoginOutPopupWindow.hide();
                    }
                })
                .build();
    }

}
=======
package com.zhiyicx.thinksnsplus.modules.settings;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.AboutUsActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class SettingsFragment extends TSFragment<SettingsContract.Presenter> implements SettingsContract.View {

    @BindView(R.id.bt_login_out)
    CombinationButton mBtLoginOut;
    @BindView(R.id.bt_set_vertify)
    CombinationButton mBtSetVertify;
    @BindView(R.id.bt_change_password)
    CombinationButton mBtChangePassword;
    @BindView(R.id.bt_clean_cache)
    CombinationButton mBtCleanCache;
    @BindView(R.id.bt_about_us)
    CombinationButton mBtAboutUs;

    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框
    private ActionPopupWindow mLoginOutPopupWindow;// 退出登录选择弹框

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
        mPresenter.getDirCacheSize();// 获取缓存大小
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
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
        ToastUtils.showToast(message);
    }

    @Override
    public void setCacheDirSize(String size) {
        mBtCleanCache.setRightText(size);
    }

    private void initListener() {
        // 认证
        RxView.clicks(mBtSetVertify)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showMessage("vertify");
                    }
                });
        // 修改密码
        RxView.clicks(mBtChangePassword)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    }
                });
        // 清理缓存
        RxView.clicks(mBtCleanCache)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initCleanCachePopupWindow();
                        mCleanCachePopupWindow.show();
                    }
                });
        // 关于我们
        RxView.clicks(mBtAboutUs)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    }
                });
        // 退出登录
        RxView.clicks(mBtLoginOut)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initLoginOutPopupWindow();
                        mLoginOutPopupWindow.show();
                    }
                });
    }


    /**
     * 初始化清理缓存选择弹框
     */
    private void initCleanCachePopupWindow() {
        if (mCleanCachePopupWindow != null) {
            return;
        }
        mCleanCachePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.clean_cache))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mPresenter.cleanCache();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mCleanCachePopupWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化清理缓存选择弹框
     */
    private void initLoginOutPopupWindow() {
        if (mLoginOutPopupWindow != null) {
            return;
        }
        mLoginOutPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.login_out))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        if(mPresenter.loginOut()){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        mLoginOutPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mLoginOutPopupWindow.hide();
                    }
                })
                .build();
    }

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94

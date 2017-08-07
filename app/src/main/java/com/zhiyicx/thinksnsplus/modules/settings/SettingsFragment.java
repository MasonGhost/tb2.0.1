package com.zhiyicx.thinksnsplus.modules.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.settings.account.AccountManagementActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.config.ApiConfig.URL_ABOUT_US;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment.BUNDLE_IS_FROM_REGISTER;

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
    @BindView(R.id.bt_account_manager)
    CombinationButton mBtAccountManager;

    //    private AlertDialog.Builder mLoginoutDialogBuilder;// 退出登录选择弹框
//    private AlertDialog.Builder mCleanCacheDialogBuilder;// 清理缓存选择弹框
    private ActionPopupWindow mLoginoutPopupWindow;// 退出登录选择弹框
    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
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
        mBtAboutUs.setRightText("V" + DeviceUtils.getVersionName(getContext()));
    }

    @Override
    public void setCacheDirSize(String size) {
        mBtCleanCache.setRightText(size);
    }

    private void initListener() {
        // 认证
        RxView.clicks(mBtSetVertify)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> showSnackSuccessMessage("vertify"));
        // 账户管理页面
        RxView.clicks(mBtAccountManager)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转账户管理页面
                    Intent intent = new Intent(getActivity(), AccountManagementActivity.class);
                    startActivity(intent);
                });
        // 修改密码
        RxView.clicks(mBtChangePassword)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> startActivity(new Intent(getActivity(), ChangePasswordActivity.class)));
        // 清理缓存
        RxView.clicks(mBtCleanCache)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    initCleanCachePopupWindow();
                    mCleanCachePopupWindow.show();
                });
        // 关于我们
        RxView.clicks(mBtAboutUs)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> CustomWEBActivity.startToWEBActivity(getContext(), URL_ABOUT_US, "lalala"));
        // 退出登录
        RxView.clicks(mBtLoginOut)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    initLoginOutPopupWindow();
                    mLoginoutPopupWindow.show();
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
                .item1Str(getString(R.string.is_sure_clean_cache))
                .item2Str(getString(R.string.sure))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mPresenter.cleanCache();
                    mCleanCachePopupWindow.hide();
                })
                .bottomClickListener(() -> mCleanCachePopupWindow.hide()).build();

    }

    /**
     * 初始化登录选择弹框
     */
    private void initLoginOutPopupWindow() {
        if (mLoginoutPopupWindow != null) {
            return;
        }
        mLoginoutPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.is_sure_login_out))
                .item2Str(getString(R.string.login_out_sure))
                .item2Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    if (mPresenter.loginOut()) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    mLoginoutPopupWindow.hide();
                })
                .bottomClickListener(() -> mLoginoutPopupWindow.hide()).build();

    }
//    /**
//     * 初始化清理缓存选择弹框
//     */
//    private void initCleanCachePopupWindow() {
//
//        if (mCleanCacheDialogBuilder == null) {
//            mCleanCacheDialogBuilder = new AlertDialog.Builder(getActivity());
//        }
//        DialogUtils.getDialog(mCleanCacheDialogBuilder, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                mPresenter.cleanCache();
//            }
//        }, getString(R.string.clean_cache), getString(R.string.is_sure_clean_cache), getString(R.string.cancel), getString(R.string.sure));
//        mCleanCacheDialogBuilder.create().show();
//    }

    /**
     * 初始化登录选择弹框
     */
//    private void initLoginOutPopupWindow() {
//
//        if (mLoginoutDialogBuilder == null) {
//            mLoginoutDialogBuilder = new AlertDialog.Builder(getActivity());
//        }
//        DialogUtils.getDialog(mLoginoutDialogBuilder, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (mPresenter.loginOut()) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//            }
//        }, getString(R.string.login_out), getString(R.string.is_sure_login_out), getString(R.string.cancel), getString(R.string.sure));
//        mLoginoutDialogBuilder.create().show();
//    }

}

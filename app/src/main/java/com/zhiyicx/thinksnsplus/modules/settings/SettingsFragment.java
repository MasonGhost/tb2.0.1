package com.zhiyicx.thinksnsplus.modules.settings;

import android.content.Intent;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.AboutUsActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

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

    //    private AlertDialog.Builder mLoginoutDialogBuilder;// 退出登录选择弹框
//    private AlertDialog.Builder mCleanCacheDialogBuilder;// 清理缓存选择弹框
    private ActionPopupWindow mLoginoutPopupWindow;// 退出登录选择弹框
    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框

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
                        showSnackSuccessMessage("vertify");
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
                        mLoginoutPopupWindow.show();
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
                .item1Str(getString(R.string.is_sure_clean_cache))
                .item2Str(getString(R.string.sure))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        mPresenter.cleanCache();
                        mCleanCachePopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mCleanCachePopupWindow.hide();
                    }
                }).build();

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
                .item2StrColor(R.color.important_for_note)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        if (mPresenter.loginOut()) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        mLoginoutPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mLoginoutPopupWindow.hide();
                    }
                }).build();

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

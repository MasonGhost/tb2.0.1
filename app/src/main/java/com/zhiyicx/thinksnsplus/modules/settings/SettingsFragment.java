package com.zhiyicx.thinksnsplus.modules.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.zhiyicx.appupdate.AppUpdateManager;
import com.zhiyicx.appupdate.AppUtils;
import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.appupdate.CustomVersionDialogActivity;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.settings.account.AccountManagementActivity;
import com.zhiyicx.thinksnsplus.widget.CheckVersionPopupWindow;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.config.ApiConfig.URL_ABOUT_US;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Describe
 * @author Jungle68
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
    @BindView(R.id.bt_check_version)
    CombinationButton mBtCheckVersion;
    // 服务器切换使用
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    private boolean mIsDefualtCheck = true;

    //    private AlertDialog.Builder mLoginoutDialogBuilder;// 退出登录选择弹框
//    private AlertDialog.Builder mCleanCacheDialogBuilder;// 清理缓存选择弹框
    private ActionPopupWindow mLoginoutPopupWindow;// 退出登录选择弹框
    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框
    private CheckVersionPopupWindow mCheckVersionPopupWindow;

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
        if (com.zhiyicx.common.BuildConfig.USE_DOMAIN_SWITCH) {
            mRbDaysGroup.setVisibility(View.VISIBLE);
            mRbOne.setVisibility(View.VISIBLE);
            mRbOne.setText(getString(R.string.domain_formal));
            mRbTwo.setVisibility(View.VISIBLE);
            mRbTwo.setText(getString(R.string.domain_test));
            mRbThree.setVisibility(View.VISIBLE);
            mRbThree.setText(getString(R.string.domain_dev));
            switch (ApiConfig.APP_DOMAIN) {
                case ApiConfig.APP_DOMAIN_FORMAL:
                    mRbOne.setChecked(true);
                    break;

                case ApiConfig.APP_DOMAIN_TEST:
                    mRbTwo.setChecked(true);

                    break;

                case ApiConfig.APP_DOMAIN_DEV:
                    mRbThree.setChecked(true);

                    break;
                default:
            }
            mTvChooseTip.setText(R.string.domain_swith);

            RxRadioGroup.checkedChanges(mRbDaysGroup)
                    .subscribe(checkedId -> {
                        if (mIsDefualtCheck) {
                            mIsDefualtCheck = false;
                            return;
                        }
                        String domain = null;
                        switch (checkedId) {
                            case R.id.rb_one:
                                domain = ApiConfig.APP_DOMAIN_FORMAL;
                                break;
                            case R.id.rb_two:
                                domain = ApiConfig.APP_DOMAIN_TEST;

                                break;
                            case R.id.rb_three:
                                domain = ApiConfig.APP_DOMAIN_DEV;
                                break;
                            default:
                        }
                        if (!TextUtils.isEmpty(domain) && mPresenter != null && getContext() != null) {
                            SharePreferenceUtils.saveString(getContext().getApplicationContext(), SharePreferenceUtils.SP_DOMAIN, domain);
                            mPresenter.loginOut();
                            Intent mStartActivity = new Intent(getContext(), GuideActivity.class);
                            int mPendingIntentId = 123456;
                            PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId, mStartActivity,
                                    PendingIntent
                                            .FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            System.exit(0);
                        }

                    });

        } else {
            mRbDaysGroup.setVisibility(View.GONE);
            mTvChooseTip.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {
        mPresenter.getDirCacheSize();// 获取缓存大小
        mBtCheckVersion.setRightText("V" + DeviceUtils.getVersionName(getContext()));
    }

    @Override
    public void setCacheDirSize(String size) {
        mBtCleanCache.setRightText(size);
    }

    @Override
    public void getAppNewVersionSuccess(List<AppVersionBean> appVersionBean) {
        if (appVersionBean != null
                && !appVersionBean.isEmpty()
                && AppUtils.getVersionCode(getContext()) < appVersionBean.get(0).getVersion_code()) {
            SharePreferenceUtils.saveObject(getContext(), CustomVersionDialogActivity.SHAREPREFERENCE_TAG_ABORD_VERION, null);
            AppUpdateManager.getInstance(getContext()
                    , ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_GET_APP_VERSION + "?version_code=" + DeviceUtils.getVersionCode(getContext
                            ()) + "&type=android")
                    .startVersionCheck();
        } else {
            showSnackSuccessMessage(getString(R.string.no_new_version));
        }
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
                .subscribe(aVoid -> CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.APP_DOMAIN + URL_ABOUT_US, getString(R.string.about_us)));
        // 退出登录
        RxView.clicks(mBtLoginOut)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    initLoginOutPopupWindow();
                    mLoginoutPopupWindow.show();
                });
        // 检查版本是否有更新
        RxView.clicks(mBtCheckVersion)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
//                    initCheckVersionPopWindow();
//                    mCheckVersionPopupWindow.show();
                    mPresenter.checkUpdate();
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
                .item1Str(String.format(getString(R.string.is_sure_clean_cache), mBtCleanCache.getRightText()))
                .item2Str(getString(R.string.determine))
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
                .backgroundAlpha(POPUPWINDOW_ALPHA)
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

}

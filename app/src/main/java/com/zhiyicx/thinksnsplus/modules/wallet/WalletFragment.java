package com.zhiyicx.thinksnsplus.modules.wallet;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletFragment extends TSFragment<WalletContract.Presenter> implements WalletContract.View {

    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bt_charge)
    CombinationButton mBtCharge;
    @BindView(R.id.bt_withdraw)
    CombinationButton mBtWithdraw;
    @BindView(R.id.tv_charge_and_withdraw_rule)
    TextView mTvChargeAndWithdrawRule;


    private ActionPopupWindow mLoginoutPopupWindow;// 退出登录选择弹框
    private ActionPopupWindow mCleanCachePopupWindow;// 清理缓存选择弹框

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.wallet);
    }

//    @Override
//    protected int setToolBarBackgroud() {
//        return R.color.white;
//    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
    }

    private void initListener() {
        // 充值
        RxView.clicks(mBtCharge)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSnackSuccessMessage("mBtCharge");
                    }
                });
        // 提现
        RxView.clicks(mBtWithdraw)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSnackSuccessMessage("mBtWithdraw");
                    }
                });
        // 充值提现规则
        RxView.clicks(mTvChargeAndWithdrawRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSnackSuccessMessage("mTvChargeAndWithdrawRule");
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
                .item2StrColor(ContextCompat.getColor(getContext(), R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
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

}

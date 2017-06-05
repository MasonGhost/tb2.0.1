package com.zhiyicx.thinksnsplus.modules.feedback;

import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/02/16:05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class FeedBackFragment extends TSFragment<FeedBackContract.Presenter> implements FeedBackContract.View {

    @BindView(R.id.et_feedback_content)
    UserInfoInroduceInputView mEtDynamicContent;
    @BindView(R.id.tv_feedback_contract)
    EditText mTvFeedbackContract;

    private ActionPopupWindow mFeedBackInstructionsPopupWindow;

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.feed_back);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.submit);
    }

    @Override
    protected void initView(View rootView) {
        initRightSubmit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_feedback;
    }

    private void initRightSubmit() {
        mToolbarRight.setEnabled(false);

        Observable.combineLatest(RxTextView.textChanges(mTvFeedbackContract), RxTextView.textChanges(mEtDynamicContent.getEtContent()),
                new Func2<CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                        return charSequence.toString().replaceAll(" ", "").length() > 0 && charSequence2.toString().replaceAll(" ", "").length() > 0;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean enable) {
                        mToolbarRight.setEnabled(enable);
                    }

                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        RxView.clicks(mToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DeviceUtils.hideSoftKeyboard(getContext(), mEtDynamicContent);
                        mPresenter.submitFeedBack(mEtDynamicContent.getInputContent(), mTvFeedbackContract.getText().toString());
                    }
                });
    }

    @Override
    public void showWithdrawalsInstructionsPop() {
        if (mFeedBackInstructionsPopupWindow != null) {
            mFeedBackInstructionsPopupWindow.show();
            return;
        }
        mFeedBackInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.input_instructions))
                .desStr(getString(R.string.input_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mFeedBackInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mFeedBackInstructionsPopupWindow.show();
    }

}

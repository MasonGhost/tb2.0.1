package com.zhiyicx.thinksnsplus.modules.feedback;

import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import butterknife.BindView;
import rx.functions.Action1;

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
        mEtDynamicContent.setContentChangedListener(new UserInfoInroduceInputView
                .ContentChangedListener() {
            @Override
            public void contentChanged(CharSequence s) {
                mToolbarRight.setEnabled(s.toString().replaceAll(" ", "").length() > 0);
            }
        });
        RxTextView.textChanges(mTvFeedbackContract).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {

            }
        });
    }

}

package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import butterknife.BindView;
import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/09/15/9:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateTopicFragment extends TSFragment<CreateTopicContract.Presenter>
        implements CreateTopicContract.View {

    @BindView(R.id.et_topic_title)
    UserInfoInroduceInputView mEtTopicTitle;
    @BindView(R.id.et_topic_desc)
    UserInfoInroduceInputView mEtTopicDesc;

    public static CreateTopicFragment getInstance() {
        return new CreateTopicFragment();
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.submit);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.create_topic);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mPresenter.createTopic(mEtTopicTitle.getInputContent(), mEtTopicDesc.getInputContent());
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS && getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    protected void initView(View rootView) {
        Observable.combineLatest(RxTextView.textChanges(mEtTopicTitle.getEtContent()),
                RxTextView.textChanges(mEtTopicDesc.getEtContent()),
                (charSequence, charSequence2) -> charSequence.length() * charSequence2.length() > 0)
                .subscribe(aBoolean -> mToolbarRight.setEnabled(aBoolean));

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_createtopic;
    }

}

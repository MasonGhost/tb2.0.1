package com.zhiyicx.thinksnsplus.modules.q_a.answer;

import android.os.Bundle;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentFragment;

/**
 * @Author Jliuer
 * @Date 2017/08/15/16:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishAnswerFragment extends PublishContentFragment {

    public static final String BUNDLE_SOURCE_ID = "source_id";

    public static PublishAnswerFragment newInstance(Bundle bundle) {
        if (bundle == null || bundle.getLong(BUNDLE_SOURCE_ID) <= 0) {
            throw new IllegalArgumentException("questin_id can not be null");
        }
        PublishAnswerFragment publishContentFragment = new PublishAnswerFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish_answer);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_btn);
    }

    @Override
    protected void setRightClick() {
        mPresenter.publishAnswer(getArguments().getLong(BUNDLE_SOURCE_ID), getContentString(), mAnonymity);

    }

    @Override
    public void publishSuccess(QAAnswerBean answerBean) {
        getActivity().finish();
    }
}

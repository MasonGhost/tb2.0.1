package com.zhiyicx.thinksnsplus.modules.q_a.answer;

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
        mPresenter.publishAnswer(getContentString(), mAnonymity);
    }

    @Override
    public void publishSuccess(QAAnswerBean answerBean) {
        getActivity().finish();
    }
}

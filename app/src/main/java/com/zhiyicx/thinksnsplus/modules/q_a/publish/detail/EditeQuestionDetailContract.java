package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface EditeQuestionDetailContract {
    interface View extends MarkdownContract.View<Presenter>{
        void publishSuccess(AnswerInfoBean answerBean);

        void updateSuccess();
    }
    interface Presenter extends MarkdownContract.Presenter{
        void publishAnswer(Long questionId, String body, int anonymity);

        void updateAnswer(Long answerId, String body, int anonymity);

        void updateQuestion(Long questionId, String body, int anonymity);

        void deleteAnswer(AnswerDraftBean answer);

        void saveAnswer(AnswerDraftBean answerDraftBean);
    }
}

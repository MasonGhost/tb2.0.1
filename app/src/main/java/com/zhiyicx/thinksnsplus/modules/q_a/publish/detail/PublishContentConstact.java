package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PublishContentConstact {
    interface View extends IBaseView<Presenter> {
        void uploadPicSuccess(int id);

        void publishSuccess(AnswerInfoBean answerBean);

        void updateSuccess();

        void uploadPicFailed();

        void addImageViewAtIndex(String iamge,int iamge_id,String markdonw,boolean isLast);

        void addEditTextAtIndex(String text);

        void onPareseBodyEnd(boolean hasContent);
    }

    interface Presenter extends IBaseTouristPresenter {
        void uploadPic(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight);

        void publishAnswer(Long question_id,String body, int anonymity);
        void updateAnswer(Long answer_id,String body, int anonymity);
        void updateQuestion(Long question_id,String body, int anonymity);

        void pareseBody(String body);

        void saveQuestion(QAPublishBean qestion);
        QAPublishBean getDraftQuestion(long qestion_mark);

        void saveAnswer(AnswerDraftBean answer);
        void deleteAnswer(AnswerDraftBean answer);
        QAPublishBean getDraftAnswer(long answer_mark);
    }

}

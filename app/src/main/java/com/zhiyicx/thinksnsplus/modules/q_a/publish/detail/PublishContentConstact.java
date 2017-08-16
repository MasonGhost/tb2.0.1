package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
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

        void publishSuccess(QAAnswerBean answerBean);

        void uploadPicFailed();
    }

    interface Presenter extends IBaseTouristPresenter {
        void uploadPic(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight);

        void publishAnswer(Long question_id,String body, int anonymity);
    }

    interface Repository extends IBasePublishQuestionRepository {
        Observable<BaseJsonV2<QAAnswerBean>> publishAnswer(Long question_id,String body, int anonymity);
    }
}

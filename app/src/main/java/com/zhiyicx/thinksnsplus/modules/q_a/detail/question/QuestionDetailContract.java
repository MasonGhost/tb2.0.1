package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public interface QuestionDetailContract {

    interface View extends ITSListView<AnswerInfoBean, Presenter>{
        void setQuestionDetail(QAListInfoBean questionDetail);
        QAListInfoBean getCurrentQuestion();
        String getCurrentOrderType();
        int getRealSize();
        void updateFollowState();
        void deleteQuestion(boolean isLoading, boolean success, String message);
    }

    interface Presenter extends ITSListPresenter<AnswerInfoBean>{
        void getQuestionDetail(String questionId);
        void handleFollowState(String questionId, boolean isFollowed);
        void shareQuestion(Bitmap bitmap);
        void deleteQuestion(Long question_id);
        void applyForExcellent(Long question_id, boolean isExcellent);
    }

    interface Repository extends IBasePublishQuestionRepository{
        Observable<QAListInfoBean> getQuestionDetail(String questionId);
        Observable<List<AnswerInfoBean>> getAnswerList(String questionId, String order_type, int size);
        Observable<BaseJsonV2<Object>> deleteQuestion(Long question_id);
        void applyForExcellent(Long question_id, boolean isExcellent);
    }
}

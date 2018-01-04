package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
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
        void setQuestionDetail(QAListInfoBean questionDetail, boolean isLoadMore);
        QAListInfoBean getCurrentQuestion();
        String getCurrentOrderType();
//        int getRealSize();
        void updateFollowState();
        void updateAnswerCount();
        void handleLoading(boolean isLoading, boolean success, String message);
    }

    interface Presenter extends ITSListPresenter<AnswerInfoBean>{
        void getQuestionDetail(String questionId,Long maxId, boolean isLoadMore);
        void handleFollowState(String questionId, boolean isFollowed);
        void shareQuestion(Bitmap bitmap);
        void saveQuestion(QAPublishBean qaPublishBean);
        void deleteQuestion(Long question_id);
        void applyForExcellent(Long question_id);
        void handleAnswerLike(boolean isLiked, final long answer_id, AnswerInfoBean answerInfoBean);
        void payForOnlook(long answer_id,int position);
        SystemConfigBean getSystemConfig();
    }

}

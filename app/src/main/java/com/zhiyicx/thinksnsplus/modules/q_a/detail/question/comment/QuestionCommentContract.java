package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public interface QuestionCommentContract {

    interface View extends ITSListView<QuestionCommentBean, Presenter>{
        QAListInfoBean getCurrentQuestion();
        void updateCommentCount();
        void setLoading(boolean isLoading, boolean isSuccess, String message);
    }

    interface Presenter extends ITSListPresenter<QuestionCommentBean>{
        void sendComment(int reply_id, String content);
        void deleteComment(long question_id, long answer_id, int position);
    }

}

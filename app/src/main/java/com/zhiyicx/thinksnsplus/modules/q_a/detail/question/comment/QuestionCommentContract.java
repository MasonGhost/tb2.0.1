package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
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
    }

    interface Presenter extends ITSListPresenter<QuestionCommentBean>{

    }

    interface Repository extends IBasePublishQuestionRepository{
        Observable<List<QuestionCommentBean>> getQuestionCommentList(Long question_Id, Long max_id);
    }
}

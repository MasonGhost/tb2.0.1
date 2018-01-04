package com.zhiyicx.thinksnsplus.modules.q_a.mine.answer;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public interface MyAnswerContract {

    interface View extends ITSListView<AnswerInfoBean, Presenter>{
        String getType();
        void updateList(int position, AnswerInfoBean answerInfoBean);
    }

    interface Presenter extends ITSListPresenter<AnswerInfoBean>{
        void handleLike(int position, AnswerInfoBean answerInfoBean);
    }

}

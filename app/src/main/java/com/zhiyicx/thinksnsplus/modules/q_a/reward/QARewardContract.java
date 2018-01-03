package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public interface QARewardContract {

    interface View extends IBaseView<Presenter>{
        void resetRewardSuccess();
        void publishQuestionSuccess(QAListInfoBean qaListInfoBean);
    }

    interface Presenter extends IBaseTouristPresenter{
        void publishQuestion(QAPublishBean qaPublishBean);
        void resetReward(Long question_id, double amount);

        void saveQuestion(QAPublishBean qestion);
        QAPublishBean getDraftQuestion(long qestion_mark);
    }

}

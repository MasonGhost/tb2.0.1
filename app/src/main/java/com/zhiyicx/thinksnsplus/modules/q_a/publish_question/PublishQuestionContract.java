package com.zhiyicx.thinksnsplus.modules.q_a.publish_question;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseQuizRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface PublishQuestionContract {

    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBasePresenter {

    }

    interface Repository extends IBaseQuizRepository {

    }
}

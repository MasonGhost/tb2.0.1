package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.QA_LIstInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface PublishQuestionContract {

    interface View extends ITSListView<QA_LIstInfoBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<QA_LIstInfoBean> {

    }

    interface Repository extends IBaseQuestionRepository {

    }
}

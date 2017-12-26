package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public interface MyPublishQuestionContract {

    interface View extends ITSListView<QAListInfoBean, Presenter>{
        String getMyQuestionType();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean>{

    }

}

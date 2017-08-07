package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public interface ExpertSearchContract {

    interface View extends ITSListView<ExpertBean, Presenter>{

    }

    interface Presenter extends ITSListPresenter<ExpertBean>{

    }

    interface Repository extends IBasePublishQuestionRepository{

    }
}

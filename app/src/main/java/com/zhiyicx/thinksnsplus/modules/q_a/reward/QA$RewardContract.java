package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseQuizRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public interface QA$RewardContract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBasePresenter{

    }

    interface Repository extends IBaseQuizRepository {

    }
}

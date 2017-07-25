package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseQuestionRepository;

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

    interface Repository extends IBaseQuestionRepository {

    }
}

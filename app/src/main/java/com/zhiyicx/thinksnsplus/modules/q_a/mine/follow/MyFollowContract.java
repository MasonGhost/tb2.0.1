package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public interface MyFollowContract {

    interface View extends ITSListView<BaseListBean, Presenter>{

    }

    interface Presenter extends ITSListPresenter<BaseListBean>{

    }

    interface Repository extends IBasePublishQuestionRepository{

    }
}

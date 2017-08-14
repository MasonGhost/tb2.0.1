package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public interface TopicDetailContract {

    interface View extends ITSListView<QAListInfoBean, Presenter>{

    }

    interface Presenter extends ITSListPresenter<QAListInfoBean>{

    }

    interface Repository{

    }
}

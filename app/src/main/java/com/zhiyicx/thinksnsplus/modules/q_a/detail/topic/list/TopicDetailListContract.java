package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailContract;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public interface TopicDetailListContract {

    interface View extends ITSListView<QAListInfoBean, Presenter> {
        String getCurrentType();
        Long getTopicId();
        void showDeleteSuccess();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean> {

    }

    interface Repository extends IBasePublishQuestionRepository {
    }
}

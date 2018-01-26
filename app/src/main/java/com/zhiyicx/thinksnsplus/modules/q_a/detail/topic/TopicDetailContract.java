package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public interface TopicDetailContract {

    interface View extends IBaseView< Presenter> {
        void setTopicDetail(QATopicBean qaTopicBean);
        Long getTopicId();
        String getCurrentType();
        QATopicBean getCurrentTopicBean();
        void updateFollowState();
        void showDeleteSuccess();
    }

    interface Presenter extends IBasePresenter{
        void getTopicDetail(String topic_id);
        void saveQuestion(QAPublishBean qaPublishBean);
        void handleTopicFollowState(String topic_id, boolean isFollow);
        void shareTopic(Bitmap bitmap);
    }

}

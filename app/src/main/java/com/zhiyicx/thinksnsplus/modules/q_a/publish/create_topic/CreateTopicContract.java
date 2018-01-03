package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/09/15/9:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CreateTopicContract {
    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBaseTouristPresenter {
        void createTopic(String name,String desc);
    }

}

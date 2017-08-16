package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailRepository extends BasePublishQuestionRepository implements QuestionDetailContract.Repository{

    @Inject
    public QuestionDetailRepository(ServiceManager manager) {
        super(manager);
    }
}

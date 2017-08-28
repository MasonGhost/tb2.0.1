package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyPublishQuestionRepository extends BaseQARepository implements MyPublishQuestionContract.Repository{

    @Inject
    public MyPublishQuestionRepository(ServiceManager manager) {
        super(manager);
    }
}

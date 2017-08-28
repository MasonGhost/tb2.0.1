package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerRepository extends BaseQARepository implements MyAnswerContract.Repository{

    @Inject
    public MyAnswerRepository(ServiceManager manager) {
        super(manager);
    }
}

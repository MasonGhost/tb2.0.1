package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.quiz.edit.SendQuizContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class SendQuizRepository extends BaseQuizRepository implements SendQuizContract.Repository{

    @Inject
    public SendQuizRepository(ServiceManager manager) {
        super(manager);
    }
}

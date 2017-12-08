package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.mine.mycode.MyCodeContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe 我的二维码
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class MyCodeRepository implements MyCodeContract.Repository{

    @Inject
    public MyCodeRepository(ServiceManager manager) {
    }
}

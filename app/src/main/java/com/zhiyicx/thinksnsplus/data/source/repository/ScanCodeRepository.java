package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeRepository implements ScanCodeContract.Repository{

    @Inject
    public ScanCodeRepository(ServiceManager manager) {

    }
}

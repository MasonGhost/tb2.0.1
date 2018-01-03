package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import dagger.Module;
import dagger.Provides;

//import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */
@Module
public class HeadPortraitViewPresenterModule {

    private HeadPortraitViewContract.View mView;

    public HeadPortraitViewPresenterModule(HeadPortraitViewContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public HeadPortraitViewContract.View provideHeadPortraitViewContractView() {
        return mView;
    }

}

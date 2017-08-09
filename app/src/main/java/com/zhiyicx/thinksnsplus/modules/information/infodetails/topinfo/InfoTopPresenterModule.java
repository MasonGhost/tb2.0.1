package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoTopRepsotory;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/05/23/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoTopPresenterModule {

    InfoTopContract.View mView;

    public InfoTopPresenterModule(InfoTopContract.View view) {
        this.mView = view;
    }

    @Provides
    InfoTopContract.View provideInfoTopContractView() {
        return mView;
    }

    @Provides
    InfoTopContract.Repository provideInfoTopContractRepository(InfoTopRepsotory infoTopRepsotory) {
        return infoTopRepsotory;
    }
}

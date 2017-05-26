package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoDetailsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoDetailsPresenterMudule {

    InfoDetailsConstract.View mView;

    public InfoDetailsPresenterMudule(InfoDetailsConstract.View view) {
        mView = view;
    }

    @Provides
    InfoDetailsConstract.View provideInfoDetailsView(){
        return mView;
    }

    @Provides
    InfoDetailsConstract.Repository provideInfoDetailsRepository(InfoDetailsRepository infoDetailsRepository){
        return infoDetailsRepository;
    }
}

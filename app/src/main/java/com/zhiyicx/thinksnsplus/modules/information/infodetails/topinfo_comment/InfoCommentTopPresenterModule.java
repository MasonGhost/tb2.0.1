package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo_comment;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoCommentTopRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoCommentTopPresenterModule {

    InfoCommentTopContract.View mView;

    public InfoCommentTopPresenterModule(InfoCommentTopContract.View view) {
        mView = view;
    }

    @Provides
    InfoCommentTopContract.View provideInfoCommentTollContractView() {
        return mView;
    }

    @Provides
    InfoCommentTopContract.Repository provideInfoCommentTollContractRepository(InfoCommentTopRepository infoCommentTopRepository) {
        return infoCommentTopRepository;
    }
}

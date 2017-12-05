package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import com.zhiyicx.thinksnsplus.data.source.repository.CircleDetailRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CirclePostDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jliuer
 * @Date 2017/12/05/10:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CirclePostDetailPresenterModule {
    CirclePostDetailContract.View mView;

    public CirclePostDetailPresenterModule(CirclePostDetailContract.View view) {
        mView = view;
    }

    @Provides
    CirclePostDetailContract.View provideCirclePostDetailContractView(){
        return mView;
    }

    @Provides
    CirclePostDetailContract.Repository provideRepository(CirclePostDetailRepository repository){
        return repository;
    }
}

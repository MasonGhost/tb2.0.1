package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.thinksnsplus.data.source.repository.PublicshContentRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class PublishContentPresenterModule {
    PublishContentConstact.View mView;

    public PublishContentPresenterModule(PublishContentConstact.View view) {
        mView = view;
    }

    @Provides
    PublishContentConstact.View providePublishContentConstactView(){
        return mView;
    }

    @Provides
    PublishContentConstact.Repository providePublishContentConstactRepository(PublicshContentRepository publicshContentRepository){
        return publicshContentRepository;
    }
}

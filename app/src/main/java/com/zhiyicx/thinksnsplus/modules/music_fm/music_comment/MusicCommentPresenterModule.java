package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicCommentRepositroty;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MusicCommentPresenterModule {

    MusicCommentContract.View mView;

    public MusicCommentPresenterModule(MusicCommentContract.View view) {
        mView = view;
    }

    @Provides
    MusicCommentContract.View provideMusicCommentView() {
        return mView;
    }

    @Provides
    MusicCommentContract.Repository provideMusicCommentRepository(Application application,
                                                                  ServiceManager
                                                                  serviceManager) {
        return new MusicCommentRepositroty(application,serviceManager);
    }
}

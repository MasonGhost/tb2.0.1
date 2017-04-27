package com.zhiyicx.thinksnsplus.dagger;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CommonMetadataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoRecommendBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumDetailsBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.MusicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@Module
public class GreenDaoModule {

    public GreenDaoModule() {
    }

    @Provides
    public UserInfoBeanGreenDaoImpl provideUserInfoBeanGreenDaoImpl(Application application) {
        return new UserInfoBeanGreenDaoImpl(application);
    }

    @Provides
    public BackgroundRequestTaskBeanGreenDaoImpl provideBackgroundRequestTaskBeanGreenDaoImpl
            (Application application) {
        return new BackgroundRequestTaskBeanGreenDaoImpl(application);
    }

    @Provides
    public FollowFansBeanGreenDaoImpl provideFollowFansBeanGreenDaoImpl(Application application) {
        return new FollowFansBeanGreenDaoImpl(application);
    }

    @Provides
    public DynamicBeanGreenDaoImpl provideDynamicBeanGreenDaoImpl(Application application) {
        return new DynamicBeanGreenDaoImpl(application);
    }

    @Provides
    public DynamicCommentBeanGreenDaoImpl provideDynamicCommentBeanGreenDaoImpl(Application application) {
        return new DynamicCommentBeanGreenDaoImpl(application);
    }

    @Provides
    public DynamicDetailBeanGreenDaoImpl provideDynamicDetailBeanGreenDaoImpl(Application application) {
        return new DynamicDetailBeanGreenDaoImpl(application);
    }

    @Provides
    public DynamicToolBeanGreenDaoImpl provideDynamicToolBeanGreenDaoImpl(Application application) {
        return new DynamicToolBeanGreenDaoImpl(application);
    }

    @Provides
    public InfoTypeBeanGreenDaoImpl provideInfoTypeBeanGreenDaoImpl(Application application) {
        return new InfoTypeBeanGreenDaoImpl(application);
    }

    @Provides
    public InfoListBeanGreenDaoImpl provideInfoListBeanGreenDaoImpl(Application application) {
        return new InfoListBeanGreenDaoImpl(application);
    }

    @Provides
    public InfoCommentListBeanDaoImpl provideInfoCommentListBeanDaoImpl(Application application) {
        return new InfoCommentListBeanDaoImpl(application);
    }

    @Provides
    public MusicAlbumListBeanGreenDaoImpl provideMusicAlbumListDaoImpl(Application application) {
        return new MusicAlbumListBeanGreenDaoImpl(application);
    }

    @Provides
    public MusicCommentListBeanGreenDaoImpl provideMusicCommentListBeanGreenDaoImpl(Application application) {
        return new MusicCommentListBeanGreenDaoImpl(application);
    }

    @Provides
    public MusicAlbumDetailsBeanGreenDaoImpl provideMusicAlbumDetailsBeanGreenDaoImpl(Application application) {
        return new MusicAlbumDetailsBeanGreenDaoImpl(application);
    }

    @Provides
    public ChannelSubscripBeanGreenDaoImpl provideChannelSubscripBeanGreenDaoImpl(Application application) {
        return new ChannelSubscripBeanGreenDaoImpl(application);
    }

    @Provides
    public ChannelInfoBeanGreenDaoImpl provideChannelInfoBeanGreenDaoImpl(Application application) {
        return new ChannelInfoBeanGreenDaoImpl(application);
    }

    @Provides
    public InfoListDataBeanGreenDaoImpl provideInfoListDataBeanGreenDaoImpl(Application application) {
        return new InfoListDataBeanGreenDaoImpl(application);
    }

    @Provides
    public InfoRecommendBeanGreenDaoImpl provideInfoRecommendBeanGreenDaoImpl(Application application) {
        return new InfoRecommendBeanGreenDaoImpl(application);
    }

    @Provides
    public CommonMetadataBeanGreenDaoImpl provideCommonMetadataBeanGreenDaoImpl(Application application) {
        return new CommonMetadataBeanGreenDaoImpl(application);
    }
}

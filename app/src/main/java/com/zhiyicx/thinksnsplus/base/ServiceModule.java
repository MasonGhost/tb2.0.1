package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.thinksnsplus.comment.CommonCommentClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ChatInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RankClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zhiyicx on 2016/3/30.
 */
@Module
public class ServiceModule {
    /**
     * 公用相关的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    CommonClient provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonClient.class);
    }

    /**
     * 登录相关的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    LoginClient provideLoginClient(Retrofit retrofit) {
        return retrofit.create(LoginClient.class);
    }

    /**
     * 密码相关的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    PasswordClient providePasswordClient(Retrofit retrofit) {
        return retrofit.create(PasswordClient.class);
    }

    /**
     * 用户信息的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    UserInfoClient provideUserInfoClient(Retrofit retrofit) {
        return retrofit.create(UserInfoClient.class);
    }

    /**
     * 聊天信息的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    ChatInfoClient provideChatInfoClient(Retrofit retrofit) {
        return retrofit.create(ChatInfoClient.class);
    }

    /**
     * 注册相关的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    RegisterClient provideRegisterClient(Retrofit retrofit) {
        return retrofit.create(RegisterClient.class);
    }

    /**
     * 音乐FM
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    MusicClient provideMusicClient(Retrofit retrofit) {
        return retrofit.create(MusicClient.class);
    }

    /**
     * 资讯-资讯分类列表
     *
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    InfoMainClient provideInfoMainClient(Retrofit retrofit) {
        return retrofit.create(InfoMainClient.class);
    }

    /**
     * 获取粉丝关注列表网络接口
     *
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    FollowFansClient provideFollowFansClient(Retrofit retrofit) {
        return retrofit.create(FollowFansClient.class);
    }

    @Singleton
    @Provides
    DynamicClient provideDynamicClient(Retrofit retrofit) {
        return retrofit.create(DynamicClient.class);
    }

    @Singleton
    @Provides
    ChannelClient provideChannelClient(Retrofit retrofit) {
        return retrofit.create(ChannelClient.class);
    }

    @Singleton
    @Provides
    CommonCommentClient provideCommonCommentClient(Retrofit retrofit) {
        return retrofit.create(CommonCommentClient.class);
    }

    @Singleton
    @Provides
    WalletClient provideWalletClient(Retrofit retrofit) {
        return retrofit.create(WalletClient.class);
    }

    @Singleton
    @Provides
    QAClient provideQAClient(Retrofit retrofit) {
        return retrofit.create(QAClient.class);
    }


    @Singleton
    @Provides
    RankClient provideRankClient(Retrofit retrofit) {
        return retrofit.create(RankClient.class);
    }

    @Singleton
    @Provides
    CircleClient provideCircleClient(Retrofit retrofit) {
        return retrofit.create(CircleClient.class);
    }

}

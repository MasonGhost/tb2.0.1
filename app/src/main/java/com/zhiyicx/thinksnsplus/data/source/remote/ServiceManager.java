package com.zhiyicx.thinksnsplus.data.source.remote;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
public class ServiceManager {
    private CommonClient mCommonClient;
    private LoginClient mLoginClient;
    private RegisterClient mRegisterClient;
    private PasswordClient mPasswordClient;
    private UserInfoClient mUserInfoClient;
    private MusicClient mMusicClient;
    private FollowFansClient mFollowFansClient;

    /**
     * 如果需要添加 service 只需在构造方法中添加对应的 service,在提供 get 方法返回出去,只要在 ServiceModule 提供了该 service
     * Dagger2 会自行注入
     *
     * @param commonClient
     */
    @Inject
    public ServiceManager(CommonClient commonClient
            , LoginClient loginClient
            , RegisterClient registerClient
            , PasswordClient passwordClient
            , UserInfoClient userInfoClient
            , MusicClient mMusicClient
            , FollowFansClient followFansClient) {
        this.mCommonClient = commonClient;
        this.mLoginClient = loginClient;
        this.mRegisterClient = registerClient;
        this.mUserInfoClient = userInfoClient;
        this.mPasswordClient = passwordClient;
        this.mMusicClient = mMusicClient;
        this.mFollowFansClient = followFansClient;
    }

    public CommonClient getCommonClient() {
        return mCommonClient;
    }

    public LoginClient getLoginClient() {
        return mLoginClient;
    }

    public RegisterClient getRegisterClient() {
        return mRegisterClient;
    }

    public UserInfoClient getUserInfoClient() {
        return mUserInfoClient;
    }

    public PasswordClient getPasswordClient() {
        return mPasswordClient;
    }

    public MusicClient getMusicClient() {
        return mMusicClient;
    }

    public FollowFansClient getFollowFansClient() {
        return mFollowFansClient;
    }
}

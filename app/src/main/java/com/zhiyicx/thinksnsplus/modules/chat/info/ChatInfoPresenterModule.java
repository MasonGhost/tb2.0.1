package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */
@Module
public class ChatInfoPresenterModule {

    private ChatInfoContract.View mView;

    public ChatInfoPresenterModule(ChatInfoContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public ChatInfoContract.View provideChatInfoContractView(){
        return mView;
    }
}

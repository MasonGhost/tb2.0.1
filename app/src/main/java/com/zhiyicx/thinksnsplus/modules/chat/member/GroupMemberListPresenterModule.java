package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.GroupMemberListRepository;
import com.zhiyicx.thinksnsplus.modules.chat.edit.manager.GroupManagerContract;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */
@Module
public class GroupMemberListPresenterModule {

    private GroupMemberListContract.View mView;

    public GroupMemberListPresenterModule(GroupMemberListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupMemberListContract.View provideGroupManagerContractView(){
        return mView;
    }

    @Provides
    public GroupMemberListContract.Repository provideGroupManagerContractRepository(GroupMemberListRepository repository){
        return repository;
    }
}

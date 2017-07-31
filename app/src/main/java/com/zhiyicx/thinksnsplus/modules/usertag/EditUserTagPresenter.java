package com.zhiyicx.thinksnsplus.modules.usertag;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/10
 * @Contact master.jungle68@gmail.com
 */

public class EditUserTagPresenter extends BasePresenter<EditUserTagContract.Repository, EditUserTagContract.View> implements EditUserTagContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public EditUserTagPresenter(EditUserTagContract.Repository repository, EditUserTagContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void getAllTags() {
        Subscription subscription = mSystemRepository.getAllTags()
                .subscribe(new BaseSubscribeForV2<List<TagCategoryBean>>() {
                    @Override
                    protected void onSuccess(List<TagCategoryBean> data) {
                        mRootView.updateTags(data);
                    }
                });
        addSubscrebe(subscription);
    }
}

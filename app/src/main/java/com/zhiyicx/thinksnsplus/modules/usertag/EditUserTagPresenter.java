package com.zhiyicx.thinksnsplus.modules.usertag;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.TagCategoryBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserTagBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    TagCategoryBeanGreenDaoImpl mTagCategoryBeanGreenDao;

    @Inject
    UserTagBeanGreenDaoImpl mUserTagBeanGreenDao;

    @Inject
    public EditUserTagPresenter(EditUserTagContract.Repository repository, EditUserTagContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void getAllTags() {

        Subscription subscription = rx.Observable.zip(mSystemRepository.getAllTags(), mUserInfoRepository.getCurrentUserTags(), (categorys, userTags) -> {
            mTagCategoryBeanGreenDao.clearTable();
            mUserTagBeanGreenDao.clearTable();

            if (!categorys.isEmpty()) {
                mTagCategoryBeanGreenDao.saveMultiData(categorys);
                mUserTagBeanGreenDao.saveMultiData(userTags);
            }
            LogUtils.d("------",mTagCategoryBeanGreenDao.getMultiDataFromCache().toString());

            return categorys;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<TagCategoryBean>>() {
                    @Override
                    protected void onSuccess(List<TagCategoryBean> data) {
                        mRootView.updateTagsFromNet(data);
                    }
                });

        addSubscrebe(subscription);
    }
}

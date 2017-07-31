package com.zhiyicx.thinksnsplus.modules.usertag;

import android.database.sqlite.SQLiteException;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
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
            try {
                mTagCategoryBeanGreenDao.clearTable();
                mUserTagBeanGreenDao.clearTable();
            } catch (SQLiteException e) {
            }

            if (!categorys.isEmpty()) {
                mTagCategoryBeanGreenDao.saveMultiData(categorys);
                mUserTagBeanGreenDao.saveMultiData(userTags);
            }
            LogUtils.d("------", mTagCategoryBeanGreenDao.getMultiDataFromCache().toString());

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

    @Override
    public void handleCategoryTagsClick(UserTagBean userTagBean) {
        mUserTagBeanGreenDao.insertOrReplace(userTagBean);

    }

    @Override
    public void addTags(Long id, int categoryPosition, int tagPosition) {
        mUserInfoRepository.addTag(id)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.addTagSuccess(categoryPosition, tagPosition);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
    }
}

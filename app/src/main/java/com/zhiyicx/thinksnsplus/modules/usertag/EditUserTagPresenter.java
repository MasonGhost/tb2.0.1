package com.zhiyicx.thinksnsplus.modules.usertag;

import android.database.sqlite.SQLiteException;

import com.zhiyicx.common.mvp.BasePresenter;
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

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Jungle68
 * @Describea
 * @Date 2017/7/10
 * @Contact master.jungle68@gmail.com
 */

public class EditUserTagPresenter extends BasePresenter<EditUserTagContract.View> implements EditUserTagContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    TagCategoryBeanGreenDaoImpl mTagCategoryBeanGreenDao;

    @Inject
    UserTagBeanGreenDaoImpl mUserTagBeanGreenDao;

    @Inject
    public EditUserTagPresenter(EditUserTagContract.View rootView) {
        super(rootView);
    }


    @Override
    public void getAllTags() {

        Subscription subscription = Observable.zip(mSystemRepository.getAllTags(),
                mUserInfoRepository.getCurrentUserTags(), (categorys, userTags) -> {
                    try {
                        mTagCategoryBeanGreenDao.clearTable();
                        mUserTagBeanGreenDao.clearTable();
                    } catch (SQLiteException e) {
                    }

                    if (!categorys.isEmpty()) {
                        mTagCategoryBeanGreenDao.saveMultiData(categorys);

                        for (TagCategoryBean category : categorys) {
                            if (category.getTags() != null) {

                                mUserTagBeanGreenDao.saveMultiData(category.getTags());
                                // 资讯投稿 or 创建圈子的标签
                                if (mRootView.getCurrentFrom() == TagFrom.INFO_PUBLISH || mRootView.getCurrentFrom() == TagFrom.CREATE_CIRCLE) {
                                    for (UserTagBean tag : category.getTags()) {
                                        if (mRootView.getChoosedTags().contains(tag)) {
                                            tag.setMine_has(true);
                                        }
                                    }
                                }

                            }
                        }

                        for (UserTagBean userTag : userTags) {
                            userTag.setMine_has(true);
                        }
                        mUserTagBeanGreenDao.saveMultiData(userTags);
                    }
                    mRootView.updateMineTagsFromNet(userTags);
                    // 资讯投稿 or 创建圈子的标签
                    if (mRootView.getCurrentFrom() == TagFrom.INFO_PUBLISH || mRootView.getCurrentFrom() == TagFrom.CREATE_CIRCLE) {
                        return categorys;
                    }
                    return mTagCategoryBeanGreenDao.getMultiDataFromCache();
                }).subscribeOn(AndroidSchedulers.mainThread())
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

        Subscription subscription = mUserInfoRepository.addTag(id)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.addTagSuccess(categoryPosition, tagPosition);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    public void onException(Throwable e) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void deleteTag(Long id, int position) {
        Subscription subscription = mUserInfoRepository.deleteTag(id)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.deleteTagSuccess(position);
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
        addSubscrebe(subscription);
    }
}

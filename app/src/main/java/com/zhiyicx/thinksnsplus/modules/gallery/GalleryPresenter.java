package com.zhiyicx.thinksnsplus.modules.gallery;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ICommentRepository;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * @Author Jliuer
 * @Date 2017/06/29/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GalleryPresenter extends BasePresenter<ICommentRepository, GalleryConstract.View> implements GalleryConstract.Presenter {

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    public GalleryPresenter(GalleryConstract.View rootView) {
        super(null, rootView);
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    public void payNote(final Long feed_id, final int imagePosition, int note) {
        mCommentRepository.paykNote(note)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.showCenterLoading(mContext.getString(R.string.transaction_doing));
                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.hideCenterLoading();
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_id);
                        dynamicDetailBeanV2.getImages().get(imagePosition).setPaid(true);
                        mRootView.getCurrentImageBean().getToll().setPaid(true);
                        mRootView.reLoadImage();
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicDetailBeanV2);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
    }
}

package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessageCommentPresenter extends AppBasePresenter<MessageCommentContract.View> implements
        MessageCommentContract.Presenter {
    @Inject
    CommentRepository mCommentRepository;
    @Inject
    CommentedBeanGreenDaoImpl mCommentedBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    public MessageCommentPresenter(MessageCommentContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription commentSub = mUserInfoRepository.getMyComments(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<CommentedBean>>() {
                    @Override
                    protected void onSuccess(List<CommentedBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(commentSub);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (isLoadMore) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(), true);

        } else {
            mRootView.onCacheResponseSuccess(mCommentedBeanGreenDao.getMultiDataFromCache(), false);

        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CommentedBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            mCommentedBeanGreenDao.clearTable();
        }
        mCommentedBeanGreenDao.saveMultiData(data);
        return true;
    }

    @Override
    public void sendComment(int mCurrentPostion, long replyToUserId, String commentContent) {
        CommentedBean currentCommentBean = mRootView.getListDatas().get(mCurrentPostion);
        String path = CommentRepository.getCommentPath(currentCommentBean.getTarget_id(), currentCommentBean.getChannel(), currentCommentBean
                .getSource_id());
        Subscription commentSub = mCommentRepository.sendCommentV2(commentContent, replyToUserId, Long.parseLong(AppApplication
                .getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis()), path)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        requestNetData(0L, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(commentSub);

    }
}

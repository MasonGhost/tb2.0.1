package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicCommentRepositroty;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT;
import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_ING;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicCommentPresenter extends BasePresenter<MusicCommentContract.Repository,
        MusicCommentContract.View> implements MusicCommentContract.Presenter {

    @Inject
    MusicCommentRepositroty mMusicCommentRepositroty;

    @Inject
    MusicCommentListBeanGreenDaoImpl mCommentListBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MusicCommentPresenter(MusicCommentContract.Repository repository, MusicCommentContract
            .View rootView) {
        super(repository, rootView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(String music_id, Long maxId, final boolean isLoadMore) {
        Subscription subscription;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            subscription = mMusicCommentRepositroty.getMusicCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribe<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                            mCommentListBeanGreenDao.saveMultiData(data);
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
        } else {
            subscription = mMusicCommentRepositroty.getAblumCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribe<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                            mCommentListBeanGreenDao.saveMultiData(data);
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
        }
        addSubscrebe(subscription);
    }

    @Override
    public void sendComment(int reply_id, String content) {
        String path;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)){
            path=APP_PATH_MUSIC_COMMENT_FORMAT;
        }else{
            path=APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
        }
        mRepository.sendComment(mRootView.getCommentId(), content, path);

        MusicCommentListBean createComment = new MusicCommentListBean();
        createComment.setState(SEND_ING);
        createComment.setReply_to_user_id(reply_id);
        createComment.setComment_content(content);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));
        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        if (reply_id == 0) {// 回复资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id((long) reply_id);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache((long)
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        mCommentListBeanGreenDao.insertOrReplace(createComment);
        mRootView.getListDatas().add(0, createComment);
        mRootView.refreshData();
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void deleteComment(MusicCommentListBean data) {
        mCommentListBeanGreenDao.deleteSingleCache(data);
        mRepository.deleteComment(mRootView.getCommentId(),data.getComment_id());
        mRootView.getListDatas().remove(data);
        mRootView.refreshData();
    }

    @Override
    public List<MusicCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return mCommentListBeanGreenDao.getMultiDataFromCache();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicCommentListBean> data) {
        return false;
    }

}

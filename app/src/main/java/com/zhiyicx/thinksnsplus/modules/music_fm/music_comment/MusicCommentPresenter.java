package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicCommentRepositroty;
import com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment.CommentBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment.CommentCore;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

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
    public void requestNetData(final String music_id, Long maxId, final boolean isLoadMore) {
        Subscription subscription;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            subscription = mMusicCommentRepositroty.getMusicCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribe<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }
//                            List<MusicCommentListBean> localComment = mCommentListBeanGreenDao.getMyMusicComment(Integer.valueOf(music_id));
//                            if (!localComment.isEmpty()) {
//                                for (int i = 0; i < localComment.size(); i++) {
//                                    localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
//                                            .getSingleDataFromCache((long) localComment.get(i).getUser_id()));
//                                    if (localComment.get(i).getReply_to_user_id() != 0) {
//                                        localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
//                                                .getSingleDataFromCache((long) localComment.get(i)
//                                                        .getReply_to_user_id()));
//                                    }
//                                }
//                                localComment.addAll(data);
//                                data.clear();
//                                data.addAll(localComment);
//                            }
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
        } else {
            subscription = mMusicCommentRepositroty.getAblumCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribe<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }
//                            List<MusicCommentListBean> localComment = mCommentListBeanGreenDao.getMyAblumComment(Integer.valueOf(music_id));
//                            if (!localComment.isEmpty()) {
//                                for (int i = 0; i < localComment.size(); i++) {
//                                    localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
//                                            .getSingleDataFromCache((long) localComment.get(i).getUser_id()));
//                                    if (localComment.get(i).getReply_to_user_id() != 0) {
//                                        localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
//                                                .getSingleDataFromCache((long) localComment.get(i)
//                                                        .getReply_to_user_id()));
//                                    }
//                                }
//                                localComment.addAll(data);
//                                data.clear();
//                                data.addAll(localComment);
//                            }
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
        }
        addSubscrebe(subscription);
    }

    @Override
    public void sendComment(int reply_id, String content) {
        String path;
        final MusicCommentListBean createComment = new MusicCommentListBean();
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            path = APP_PATH_MUSIC_COMMENT_FORMAT;
            createComment.setMusic_id(mRootView.getCommentId());
        } else {
            path = APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
            createComment.setSpecial_id(mRootView.getCommentId());
        }
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
        if (mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().add(0, createComment);
        mRootView.refreshData();
        BackgroundTaskHandler.OnNetResponseCallBack callBack = new BackgroundTaskHandler.OnNetResponseCallBack() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.d("sendComment:::" + data.toString());
                createComment.setId(Long.parseLong(data.toString()));
                mCommentListBeanGreenDao.insertOrReplace(createComment);
            }

            @Override
            public void onFailure(String message, int code) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        };
        mRepository.sendComment(mRootView.getCommentId(), reply_id, content, path, createComment.getComment_mark(), callBack);
    }

    @Override
    public void getMusicDetails(String music_id) {
        mRepository.getMusicDetails(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicDetaisBean>() {
                    @Override
                    protected void onSuccess(MusicDetaisBean data) {
                        MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                        headerInfo.setCommentCount(data.getComment_count());
                        headerInfo.setId(data.getId());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvert(data.getSinger().getCover().getId() + "",
                                ImageZipConfig.IMAGE_70_ZIP));
                        headerInfo.setTitle(data.getTitle());
                        mRootView.setHeaderInfo(headerInfo);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void getMusicAblum(String id) {
        mRepository.getMusicAblum(id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicAlbumDetailsBean>() {
                    @Override
                    protected void onSuccess(MusicAlbumDetailsBean data) {
                        MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                        headerInfo.setCommentCount(data.getComment_count());
                        headerInfo.setId(data.getId());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvert(data.getStorage() + "", ImageZipConfig.IMAGE_70_ZIP));
                        headerInfo.setTitle(data.getTitle());
                        mRootView.setHeaderInfo(headerInfo);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void deleteComment(MusicCommentListBean data) {
        mCommentListBeanGreenDao.deleteSingleCache(data);
        CommentBean commentBean = new CommentBean();
        commentBean.setComment_id(data.getId() == null ? 1 : data.getId().intValue());
        commentBean.setNetRequestUrl(String.format(ApiConfig
                .APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, data.getId()));
        CommentCore.getInstance(CommentCore.CommentState.DELETE)
                .set$$Comment(commentBean)
                .handleComment();

//        mRepository.deleteComment(mRootView.getCommentId(),data.getComment_id());
        mRootView.getListDatas().remove(data);
        if (mRootView.getListDatas().size() == 0) {// 占位
            MusicCommentListBean emptyData = new MusicCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
    }

    @Override
    public List<MusicCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        List<MusicCommentListBean> localComment=new ArrayList<>();
//        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
//            localComment = mCommentListBeanGreenDao.getLocalMusicComment(mRootView.getCommentId());
//        } else {
//            localComment = mCommentListBeanGreenDao.getLocalAblumComment(mRootView.getCommentId());
//        }
//
//        if (!localComment.isEmpty()) {
//            for (int i = 0; i < localComment.size(); i++) {
//                localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
//                        .getSingleDataFromCache((long) localComment.get(i).getUser_id()));
//                if (localComment.get(i).getReply_to_user_id() != 0) {
//                    localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
//                            .getSingleDataFromCache((long) localComment.get(i)
//                                    .getReply_to_user_id()));
//                }
//            }
//        }
        return localComment;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicCommentListBean> data, boolean isLoadMore) {
        return false;
    }

}

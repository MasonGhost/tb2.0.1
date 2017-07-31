package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.comment.CommonMetadataBean;
import com.zhiyicx.thinksnsplus.comment.TCommonMetadataProvider;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicCommentRepositroty;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT;
import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_ERROR;
import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_ING;
import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_SUCCESS;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicCommentPresenter extends AppBasePresenter<MusicCommentContract.Repository,
        MusicCommentContract.View> implements MusicCommentContract.Presenter {

    @Inject
    MusicCommentRepositroty mMusicCommentRepositroty;

    @Inject
    CommentedBeanGreenDaoImpl mCommentListBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    CommentRepository mCommentRepository;

    private TCommonMetadataProvider mCommonMetadataProvider;

    @Inject
    public MusicCommentPresenter(MusicCommentContract.Repository repository, MusicCommentContract
            .View rootView) {
        super(repository, rootView);
        mCommonMetadataProvider = new TCommonMetadataProvider(null);
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
                    .subscribe(new BaseSubscribeForV2<List<CommentedBean>>() {
                        @Override
                        protected void onSuccess(List<CommentedBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }

//                            TCommonMetadataProvider test = new TCommonMetadataProvider(data);
//                            test.convertAndSave();

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
                    .subscribe(new BaseSubscribeForV2<List<CommentedBean>>() {
                        @Override
                        protected void onSuccess(List<CommentedBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }

//                            TCommonMetadataProvider test = new TCommonMetadataProvider(data);
//                            test.convertAndSave();

//                            List<MusicCommentListBean> localComment = mCommentListBeanGreenDao.getMyAblumComment(Integer.valueOf(music_id));
//                            if (!localComment.isEmpty()) {
//                                for (int i = 0; i < localComment.size(); i++) {
//                                    localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
//                                            .getSingleDataFromCache((long) localComment.get(i).getUser_id()));
//                                    if (localComment.get(i).getReply_user() != 0) {
//                                        localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
//                                                .getSingleDataFromCache((long) localComment.get(i)
//                                                        .getReply_user()));
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
    public void sendComment(long reply_id, String content) {
        String path;
        final CommentedBean createComment = new CommentedBean();
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            path = APP_PATH_MUSIC_COMMENT_FORMAT;
            createComment.setId(mRootView.getCommentId());
        } else {
            path = APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
            createComment.setId(mRootView.getCommentId());
        }

        createComment.setReply_user(reply_id);
        createComment.setComment_content(content);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        if (reply_id == 0) {// 回复资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(reply_id);
            createComment.setCommentUserInfo(userInfoBean);
        } else {
            createComment.setReplyUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(reply_id));
        }
        createComment.setCommentUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(
                AppApplication.getmCurrentLoginAuth().getUser_id()));

        mCommentListBeanGreenDao.insertOrReplace(createComment);
        if (mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().add(0, createComment);
        mRootView.refreshData();
        path = String.format(path, mRootView.getCommentId());

        // 更新的评论模块
//        new CommentCore_.Builder()
//                .buildCommentEvent(CommentCore_.CommentState.SEND)
//                .buildCommonMetadataAndProvider(mCommonMetadataProvider,createComment)
//                .build()
//                .handleCommentInBackGroud();

        // 新的评论模块
//        CommentCore.getInstance(CommentCore.CommentState.SEND,
//                new BackgroundTaskHandler.OnNetResponseCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_id(((Double) data).intValue());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//
//                    @Override
//                    public void onFailure(String message, int code) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//                }).set$$Comment_(createComment, mCommonMetadataProvider)
//                .handleCommentInBackGroud();

        Subscription subscription = mCommentRepository.sendCommentV2(content, reply_id, 0L, path)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                            createComment.setId(jsonObject.getJSONObject("comment").getLong("id"));
                            mCommentListBeanGreenDao.insertOrReplace(createComment);
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void reSendComment(final CommentedBean createComment) {
        String path;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            path = APP_PATH_MUSIC_COMMENT_FORMAT;
        } else {
            path = APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
        }
        mCommentListBeanGreenDao.insertOrReplace(createComment);
        if (mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().set(0, createComment);
        mRootView.refreshData();
        path = String.format(path, mRootView.getCommentId());
        Subscription subscription = mCommentRepository.sendComment(createComment.getComment_content(), createComment.getReply_user(),
                0L, path).doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                            createComment.setId(jsonObject.getJSONObject("comment").getLong("id"));
                            mCommentListBeanGreenDao.insertOrReplace(createComment);
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getMusicDetails(String music_id) {
        mRepository.getMusicDetails(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicDetaisBean>() {
                    @Override
                    protected void onSuccess(MusicDetaisBean data) {
                        MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                        headerInfo.setCommentCount(data.getComment_count());
                        headerInfo.setId(data.getId().intValue());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(data.getSinger().getCover().getId()
                                ,mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                ,mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , ImageZipConfig.IMAGE_70_ZIP));
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
                        headerInfo.setId(data.getId().intValue());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(data.getStorage().getId()
                                ,mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                ,mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , ImageZipConfig.IMAGE_70_ZIP));
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
    public void deleteComment(CommentedBean data) {
        mCommentListBeanGreenDao.deleteSingleCache(data);

        // 新的评论模块
//        CommentCore.getInstance(CommentCore.CommentState.DELETE, new CommentCore.CallBack())
//                .set$$Comment_(data,new TCommonMetadataProvider(null))
//                .handleComment();

        mRepository.deleteComment((int)mRootView.getCommentId(), data.getId().intValue());
        mRootView.getListDatas().remove(data);
        if (mRootView.getListDatas().size() == 0) {// 占位
            CommentedBean emptyData = new CommentedBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
    }

    @Override
    public List<CommentedBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        List<CommentedBean> localComment;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            localComment = mCommentListBeanGreenDao.getAblumCommentsCacheDataByType(ApiConfig.APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS,mRootView.getCommentId());
        } else {
            localComment = mCommentListBeanGreenDao.getAblumCommentsCacheDataByType(ApiConfig.APP_COMPONENT_MUSIC,mRootView.getCommentId());
        }

        if (!localComment.isEmpty()) {
            for (int i = 0; i < localComment.size(); i++) {
                localComment.get(i).setCommentUserInfo(mUserInfoBeanGreenDao
                        .getSingleDataFromCache(localComment.get(i).getUser_id()));
                if (localComment.get(i).getReply_user() != 0) {
                    localComment.get(i).setReplyUserInfo(mUserInfoBeanGreenDao
                            .getSingleDataFromCache(localComment.get(i)
                                    .getReply_user()));
                }
            }
        }
        return localComment;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CommentedBean> data, boolean isLoadMore) {
        return false;
    }

}

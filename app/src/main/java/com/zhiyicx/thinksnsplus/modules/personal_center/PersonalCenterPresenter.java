package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SendDynamicDataBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class PersonalCenterPresenter extends AppBasePresenter<PersonalCenterContract.Repository, PersonalCenterContract.View> implements PersonalCenterContract.Presenter, OnShareCallbackListener {
    private static final int NEED_INTERFACE_NUM = 3;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    IUploadRepository mIUploadRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    SendDynamicDataBeanV2GreenDaoImpl mSendDynamicDataBeanV2GreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    CommentRepository mCommentRepository;

    @Inject
    public SharePolicy mSharePolicy;

    private int mInterfaceNum = 0;//纪录请求接口数量，用于统计接口是否全部请求完成，需要接口全部请求完成后在显示界面
    SparseArray<Long> msendingStatus = new SparseArray<>();

    @Inject
    public PersonalCenterPresenter(PersonalCenterContract.Repository repository, PersonalCenterContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicDetailBeanV2> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore, final long user_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        Subscription subscription = mRepository.getDynamicListForSomeone(user_id, maxId)
                .subscribeOn(Schedulers.io())
                .map(dynamicDetailBeanV2s -> {
                    List<DynamicDetailBeanV2> result = new ArrayList<>();

                    for (DynamicDetailBeanV2 detailBeanV2 : dynamicDetailBeanV2s) {
                        if (detailBeanV2.getUser_id() == user_id) {
                            result.add(detailBeanV2);
                        }
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(listBaseJson -> {
                    if (!isLoadMore && AppApplication.getmCurrentLoginAuth().getUser_id() == user_id) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                        List<DynamicDetailBeanV2> data = getDynamicBeenFromDBV2();
                        mRootView.updateDynamicCounts(data.size());//修改动态条数
                        data.addAll(listBaseJson);
                    }
                    for (int i = 0; i < listBaseJson.size(); i++) { // 把自己发的评论加到评论列表的前面
                        List<DynamicCommentBean> dynamicCommentBeen = mDynamicCommentBeanGreenDao.getMySendingComment(listBaseJson.get(i).getFeed_mark());
                        if (!dynamicCommentBeen.isEmpty()) {
                            dynamicCommentBeen.addAll(listBaseJson.get(i).getComments());
                            listBaseJson.get(i).getComments().clear();
                            listBaseJson.get(i).getComments().addAll(dynamicCommentBeen);
                        }
                    }

                    return listBaseJson;
                }).subscribe(new BaseSubscribeForV2<List<DynamicDetailBeanV2>>() {
                    @Override
                    protected void onSuccess(List<DynamicDetailBeanV2> data) {
                        mInterfaceNum++;
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                        allready();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (mInterfaceNum >= NEED_INTERFACE_NUM) {
                            mRootView.showMessage(message);
                        } else {
                            mRootView.loadAllError();
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        if (mInterfaceNum >= NEED_INTERFACE_NUM) {
                            mRootView.onResponseError(throwable, isLoadMore);
                        } else {
                            mRootView.loadAllError();
                        }
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public List<DynamicDetailBeanV2> requestCacheData(Long max_Id, boolean isLoadMore, long user_id) {
        return mDynamicDetailBeanV2GreenDao.getMyDynamics(user_id);
    }

    @Override
    public void initFollowState(long user_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        Subscription subscription = mRepository.getUserFollowState(user_id + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<FollowFansBean>() {
                    @Override
                    protected void onSuccess(FollowFansBean data) {
                        mInterfaceNum++;
                        mRootView.setFollowState(data);
                        allready();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.loadAllError();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleFollow(FollowFansBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        // ui进行刷新
        mRootView.setFollowState(followFansBean);
    }

    @Override
    public void uploadUserCover(String filePath) {
        BitmapFactory.Options options = DrawableProvider.getPicsWHByFile(filePath);
        Subscription subscription = mIUploadRepository.upLoadSingleFileV2(
                filePath, options.outMimeType, true, options.outWidth, options.outHeight)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.setUpLoadCoverState(true, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.setUpLoadCoverState(false, 0);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadCoverState(false, 0);
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void changeUserCover(final UserInfoBean userInfoBean, int storage_task_id, final String imagePath) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cover_storage_task_id", storage_task_id + "");
        Subscription subscription = mUserInfoRepository.changeUserInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe() {

                    @Override
                    protected void onSuccess(Object data) {
                        // 修改成功后，关闭页面
                        mRootView.setChangeUserCoverState(true);
                        // 将本地图片路径作为storageId保存到数据库
                        userInfoBean.setCover(imagePath);
                        // 更新用户数据库
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 修改失败，好尴尬
                        mRootView.setChangeUserCoverState(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setChangeUserCoverState(false);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void shareUserInfo(UserInfoBean userInfoBean) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(userInfoBean.getName());
        shareContent.setContent(TextUtils.isEmpty(userInfoBean.getIntro()) ? mContext.getString(R.string.intro_default) : userInfoBean.getIntro());
        if (null != mRootView.getUserHeadPic()) {
            shareContent.setBitmap(mRootView.getUserHeadPic());
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_USERINFO, userInfoBean.getUser_id()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        mRepository.updateOrInsertDynamicV2(data, ApiConfig.DYNAMIC_TYPE_NEW);
        return true;
    }

    @Override
    public void setCurrentUserInfo(Long userId) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        Subscription subscription = mRepository.getCurrentUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        mInterfaceNum++;
                        mRootView.setHeaderInfo(data);
                        allready();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.loadAllError();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    private void allready() {
        if (mInterfaceNum == NEED_INTERFACE_NUM) {
            mRootView.allDataReady();
            mInterfaceNum = 0;
        }
    }

    @NonNull
    private List<DynamicDetailBeanV2> getDynamicBeenFromDBV2() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        List<DynamicDetailBeanV2> datas = mDynamicDetailBeanV2GreenDao.getMySendingUnSuccessDynamic((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        msendingStatus.clear();
        for (int i = 0; i < datas.size(); i++) {
            if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
                datas.get(i).setState(DynamicDetailBeanV2.SEND_ERROR);
            }
            msendingStatus.put(i, datas.get(i).getFeed_mark());
        }
        if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
            mDynamicDetailBeanV2GreenDao.insertOrReplace(datas);
        }

        return datas;
    }


    /**
     * handle like or cancle like in background
     *
     * @param isLiked true,do like ,or  cancle like
     * @param feed_id dynamic id
     * @param postion current item position
     */
    @Override
    public void handleLike(boolean isLiked, final Long feed_id, final int postion) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(postion));
        mRepository.handleLike(isLiked, feed_id);

    }

    @Override
    public void handleViewCount(Long feed_id, int position) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mRootView.getListDatas().get(position).setFeed_view_count(mRootView.getListDatas().get(position).getFeed_view_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(position));
        mRepository.handleDynamicViewCount(feed_id);
        mRootView.refreshData();
    }

    @Override
    public void reSendDynamic(int position) {
        // 将动态信息存入数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(position));
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC_V2);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", mRootView.getListDatas().get(position).getFeed_mark());
        params.put("sendDynamicDataBean", mSendDynamicDataBeanV2GreenDao.getSendDynamicDataBeanV2ByFeedMark
                (String.valueOf(mRootView.getListDatas().get(position).getFeed_mark())));
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(DynamicDetailBeanV2 dynamicBean, int dynamicPosition, long comment_id, int commentPositon) {
        mRootView.getListDatas().get(dynamicPosition).setFeed_comment_count(dynamicBean.getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPosition));
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicBean.getComments().get(commentPositon));
        mRootView.getListDatas().get(dynamicPosition).getComments().remove(commentPositon);
        mRootView.refreshData(dynamicPosition);
        mRepository.deleteComment(dynamicBean.getId(), comment_id);
    }

    @Override
    public void deleteCommentV2(DynamicDetailBeanV2 dynamicBean, int dynamicPositon, long comment_id, int commentPosition) {
        mRootView.getListDatas().get(dynamicPositon).setFeed_comment_count(dynamicBean.getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPositon));
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicBean.getComments().get(commentPosition));
        mRootView.getListDatas().get(dynamicPositon).getComments().remove(commentPosition);
        mRootView.refreshData(dynamicPositon);
        mRepository.deleteCommentV2(dynamicBean.getId(), comment_id);
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mRepository.sendCommentV2(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(),
                commentBean.getComment_mark());
        mRootView.refreshData();
    }

    @Override
    public void deleteDynamic(DynamicDetailBeanV2 dynamicBean, int position) {

        mDynamicDetailBeanV2GreenDao.deleteSingleCache(dynamicBean);
        mRootView.getListDatas().remove(position);
        mRootView.refreshData();
        if (dynamicBean.getId() != null && dynamicBean.getId() != 0) {
            mRepository.deleteDynamic(dynamicBean.getId());
        }


    }

    /**
     * send a commment
     *
     * @param mCurrentPostion current dynamic position
     * @param replyToUserId   comment  to who
     * @param commentContent  comment content
     */
    @Override
    public void sendComment(int mCurrentPostion, long replyToUserId, String commentContent) {
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getListDatas().get(mCurrentPostion).getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        List<DynamicCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getComments());
        mRootView.getListDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).setFeed_comment_count(mRootView.getListDatas().get(mCurrentPostion).getFeed_comment_count() + 1);
        mRootView.refreshData(mCurrentPostion);

        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(mCurrentPostion));
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendComment(commentContent, mRootView.getListDatas().get(mCurrentPostion)
                .getId(), replyToUserId, creatComment.getComment_mark());

    }

    @Override
    public void sendCommentV2(int mCurrentPostion, long replyToUserId, String commentContent) {
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getListDatas().get(mCurrentPostion).getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        List<DynamicCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getComments());
        mRootView.getListDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).setFeed_comment_count(mRootView.getListDatas().get(mCurrentPostion).getFeed_comment_count() + 1);
        mRootView.refreshData(mCurrentPostion);

        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(mCurrentPostion));
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendCommentV2(commentContent, mRootView.getListDatas().get(mCurrentPostion)
                .getId(), replyToUserId, creatComment.getComment_mark());
    }

    @Override
    public void handleCollect(DynamicDetailBeanV2 dynamicBean) {
        // 收藏
        // 修改数据
        boolean is_collection = dynamicBean.isHas_collect();// 旧状态
        dynamicBean.setHas_collect(!is_collection);
        boolean newCollectState = !is_collection;
        // 更新数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", dynamicBean.getId());
        // 后台处理
        if (newCollectState) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_UNCOLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        }
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getFeed_content()) ? mContext.getString(R.string
                .share_dynamic) : dynamicBean.getFeed_content());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getId()
                == null ? "" : dynamicBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public List<SystemConfigBean.Advert> getAdvert() {
        return null;
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    public void payNote(int dynamicPosition, int imagePosition, int note, boolean isImage) {
        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataByUserId((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        double balance = 0;
        if (walletBean != null) {
            balance = walletBean.getBalance();
        }
        double amount = mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition).getAmount();
        if (balance < amount) {
            mRootView.goRecharge(WalletActivity.class);
            return;
        }
        mCommentRepository.paykNote(note)
                .doOnSubscribe(() -> mRootView.showCenterLoading(mContext.getString(R.string.transaction_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.hideCenterLoading();
                        if (isImage) {
                            mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition).setPaid(true);
                        } else {
                            mRootView.getListDatas().get(dynamicPosition).getPaid_node().setPaid(true);
                        }
                        mRootView.refreshData(dynamicPosition);
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPosition));
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

    /**
     * 通过 feedMark 获取当前数据的位置
     *
     * @param feedMark
     * @return
     */
    @Override
    public int getCurrenPosiotnInDataList(long feedMark) {
        int position = -1;
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (feedMark == mRootView.getListDatas().get(i).getFeed_mark()) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {
        Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicCommentBean1.getFeed_mark())) {
                            dynamicPosition = i;
                            break;
                        }
                    }
                    if (dynamicPosition != -1) {// 如果列表有当前评论
                        int commentSize = mRootView.getListDatas().get(dynamicPosition).getComments().size();
                        for (int i = 0; i < commentSize; i++) {
                            if (mRootView.getListDatas().get(dynamicPosition).getComments().get(i).getFeed_mark().equals(dynamicCommentBean1.getFeed_mark())) {
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setState(dynamicCommentBean1.getState());
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_id(dynamicCommentBean1.getComment_id());
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_mark(dynamicCommentBean1.getComment_mark());
                                break;
                            }
                        }
                    }
                    return dynamicPosition;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData(integer);
                    }

                }, throwable -> throwable.printStackTrace());

    }

    /**
     * 动态详情处理了数据
     * 处理更新动态数据
     *
     * @param data
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_DYNAMIC)
    public void updateDynamic(Bundle data) {
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(bundle -> {
                    int position = bundle.getInt(DYNAMIC_DETAIL_DATA_POSITION);
                    boolean isNeedRefresh = bundle.getBoolean(DYNAMIC_LIST_NEED_REFRESH);
                    DynamicDetailBeanV2 dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                            dynamicPosition = i;
                            break;
                        }
                    }
                    if (dynamicPosition != -1) {// 如果列表有当前评论
                        mRootView.getListDatas().set(position, dynamicBean);
                    }

                    return isNeedRefresh ? dynamicPosition : -1;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, throwable -> throwable.printStackTrace());


    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }
}
